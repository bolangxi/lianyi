package com.ted.resonance.service.impl;


import com.ted.resonance.config.DServiceConstant;
import com.ted.resonance.config.DataDictCache;
import com.ted.resonance.config.DataDictConstant;
import com.ted.resonance.entity.*;
import com.ted.resonance.enums.OrderStatus;
import com.ted.resonance.enums.WithdrawOrderRecordStatusEnum;
import com.ted.resonance.mapper.*;
import com.ted.resonance.entity.request.WithdrawApplyRequest;
import com.ted.resonance.entity.request.WithdrawBroadcastRequest;
import com.ted.resonance.entity.response.WithdrawMessageResponse;
import com.ted.resonance.entity.response.WithdrawSignResponse;
import com.ted.resonance.service.RedisLock;
import com.ted.resonance.service.WithdrawOrderService;
import com.ted.resonance.utils.DateUtil;
import com.ted.resonance.utils.SignUtil;
import com.ted.resonance.utils.exceptions.CommonException;
import com.ted.resonance.utils.web.ResponseEntity;
import com.ted.resonance.utils.web.ResponseUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class WithdrawOrderServiceImpl implements WithdrawOrderService {


    @Autowired
    private UserAccDetailBeanMapper userAccDetailBeanMapper;

    @Autowired
    private WithdrawOrderRecordBeanMapper withdrawOrderRecordBeanMapper;

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private UserAccBeanMapper userAccBeanMapper;

    @Autowired
    private WithdrawOrderLogMapper withdrawOrderLogMapper;

    @Autowired
    private BroadcastTransactionBeanMapper broadcastTransactionBeanMapper;

    @Autowired
    @Qualifier("ethClient")
    private Web3j ethClient;

    @Autowired
    @Qualifier("etcClient")
    private Web3j etcClient;

    @Value("${private-key.eth}")
    private String privateKeyEth;

    @Value("${private-key.etc}")
    private String privateKeyEtc;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    @Transactional
    public ResponseEntity withdrawApply(WithdrawApplyRequest withDrawApplyRequest) {
        logger.info("withDrawApplyRequest{}", withDrawApplyRequest);
        if (withDrawApplyRequest == null
                || !("ETH".equalsIgnoreCase(withDrawApplyRequest.getCoinType())
                || "ETC".equalsIgnoreCase(withDrawApplyRequest.getCoinType()))
                || StringUtils.isBlank(withDrawApplyRequest.getToAddress())
                || withDrawApplyRequest.getAmount() == null
                || withDrawApplyRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new CommonException(DServiceConstant.PARAMS_ERROR.getCode(), DServiceConstant.PARAMS_ERROR.getMsg());
        }
        withDrawApplyRequest.setCoinType(withDrawApplyRequest.getCoinType().toUpperCase());
        UserAccBean userAcc = userAccBeanMapper.selectByAddrAndCoinType(withDrawApplyRequest.getToAddress(), withDrawApplyRequest.getCoinType());
        if (userAcc == null) {
            throw new CommonException(DServiceConstant.PARAMS_ERROR.getCode(), DServiceConstant.PARAMS_ERROR.getMsg());
        }

        String accBalanceLockKey = "activity-service:redisKey:accBalance" + userAcc.getId();
        Boolean locked = false;
        try {
            // 上锁
            locked = redisLock.commonLock(accBalanceLockKey, 3, TimeUnit.SECONDS);

            if (!locked) {
                throw new CommonException(DServiceConstant.PARAMS_ERROR.getCode(), DServiceConstant.PARAMS_ERROR.getMsg());
            }
            String tradeNo = getWithdrawOrderNo(new Date());

            BigDecimal aferAmount;
            aferAmount = userAcc.getBalance().subtract(withDrawApplyRequest.getAmount());
            UserAccDetailBean accDetail = new UserAccDetailBean();
            accDetail.setUserId(0L);
            accDetail.setAccId(userAcc.getId());
            accDetail.setCoinType(withDrawApplyRequest.getCoinType());
            accDetail.setAmount(withDrawApplyRequest.getAmount());
            accDetail.setPreAmount(userAcc.getBalance());
            accDetail.setAfterAmount(aferAmount);
            accDetail.setTradeType("WITHDRAW");
            accDetail.setOperateType("SUB");
            accDetail.setCreateTime(new Date());
            accDetail.setUpdateTime(new Date());
            accDetail.setRemark("提币操作");
            accDetail.setTradeStatus(OrderStatus.WITHDRAWING.name());
            accDetail.setTradeNo(tradeNo);
            accDetail.setAddr(withDrawApplyRequest.getToAddress());
            userAccDetailBeanMapper.insert(accDetail);
            //用户账户操作
            userAccBeanMapper.forzenBalanceById(withDrawApplyRequest.getAmount(), userAcc.getId());
            //生成提币记录信息
            WithdrawOrderRecordBean withdrawOrderRecordBean = new WithdrawOrderRecordBean();
            //获取地址信息
            withdrawOrderRecordBean.setAddressTo(withDrawApplyRequest.getToAddress());
            //订单号生成
            withdrawOrderRecordBean.setTradeNo(tradeNo);
            withdrawOrderRecordBean.setUserId(0);
            withdrawOrderRecordBean.setAmount(withDrawApplyRequest.getAmount());
            withdrawOrderRecordBean.setAutoApprove(Boolean.FALSE);
            withdrawOrderRecordBean.setCurrency(withDrawApplyRequest.getCoinType());
            String privateKey = "";
            int coinType = -1;
            if ("ETH".equals(withDrawApplyRequest.getCoinType())) {
                coinType = 1;
                privateKey = privateKeyEth;
                withdrawOrderRecordBean.setFee(new BigDecimal(DataDictCache.getDataValue(DataDictConstant.ETHPOUNDAGE)));
            } else if ("ETC".equals(withDrawApplyRequest.getCoinType())) {
                coinType = 0;
                privateKey = privateKeyEtc;
                withdrawOrderRecordBean.setFee(new BigDecimal(DataDictCache.getDataValue(DataDictConstant.ETCPOUNDAGE)));
            }

            if (withdrawOrderRecordBean.getAmount().compareTo(withdrawOrderRecordBean.getFee()) <= 0) {
                throw new CommonException(DServiceConstant.PARAMS_ERROR.getCode(), DServiceConstant.PARAMS_ERROR.getMsg());
            }

            withdrawOrderRecordBean.setFeeCurrency(withDrawApplyRequest.getCoinType());
            withdrawOrderRecordBean.setToken(Boolean.FALSE);
            withdrawOrderRecordBean.setParentCurrency(withDrawApplyRequest.getCoinType());
            withdrawOrderRecordBean.setType("USER");
            withdrawOrderRecordBean.setStatus(WithdrawOrderRecordStatusEnum.PAYING.name());
            withdrawOrderRecordBeanMapper.insertSelective(withdrawOrderRecordBean);


            WithdrawSignResponse withdrawSignResponse = SignUtil.signWithdraw(privateKey,
                    withDrawApplyRequest.getToAddress(),
                    withDrawApplyRequest.getAmount().subtract(withdrawOrderRecordBean.getFee()),
                    withdrawOrderRecordBean.getId(), coinType);
            withdrawSignResponse.setTradeNo(tradeNo);

            BroadcastTransactionBean broadcastTransactionBean = new BroadcastTransactionBean();
            broadcastTransactionBean.setAddTime(new Date());
            broadcastTransactionBean.setR(withdrawSignResponse.getR());
            broadcastTransactionBean.setRefId(withdrawOrderRecordBean.getId().intValue());
            broadcastTransactionBean.setS(withdrawSignResponse.getS());
            broadcastTransactionBean.setV(withdrawSignResponse.getV().intValue());
            broadcastTransactionBean.setStatus(0);//初始化
            broadcastTransactionBean.setType("WITHDRAW");
            broadcastTransactionBean.setUpdateTime(new Date());
            broadcastTransactionBean.setCurrency(withdrawOrderRecordBean.getCurrency());
            broadcastTransactionBeanMapper.insert(broadcastTransactionBean);

            return ResponseUtils.makeOkResponse(withdrawSignResponse);
        } finally {
            // 解锁
            if (locked) {
                redisLock.commonUnLock(accBalanceLockKey);
            }
        }
    }

    private String getWithdrawOrderNo(Date date) {
        String dateString = DateUtil.dateToNoFormatString(date);
        return "tr" + dateString + getRandom(8);
    }

    private String getRandom(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            val += String.valueOf(random.nextInt(10));
        }
        return val;
    }


    private Boolean withdrawStatusCallBack(String tradeNo, Boolean success) {

        WithdrawOrderRecordBean withdrawOrderRecordBean = withdrawOrderRecordBeanMapper.selectTradeNo(tradeNo);
        if (withdrawOrderRecordBean == null) {
            return Boolean.FALSE;
        }
        UserAccDetailBean userAccDetail = userAccDetailBeanMapper.selectByTradeNo(tradeNo);
        if (userAccDetail == null) {
            return Boolean.FALSE;
        }
        if (!userAccDetail.getTradeStatus().equals(OrderStatus.WITHDRAWING.name())) {
            return Boolean.TRUE;
        }
        UserAccBean userAcc = userAccBeanMapper.selectByAddrAndCoinType(withdrawOrderRecordBean.getAddressTo(), userAccDetail.getCoinType());

        if (!success) {
            //获取订单详情信息
            int num = userAccDetailBeanMapper.updateTradeStatusByNo(tradeNo, OrderStatus.FAIL.name());
            if (num == 0) {
                return Boolean.TRUE;
            }
            //增加余额
            userAccBeanMapper.addUserBalanceById(userAccDetail.getAmount(), userAcc.getId());
        } else {
            int num = userAccDetailBeanMapper.updateTradeStatusByNo(tradeNo, OrderStatus.SUCCESS.name());

            if (num == 0) {
                return Boolean.TRUE;
            }
            //减少冻结金额
            userAccBeanMapper.addUserBalanceById(userAccDetail.getAmount(), userAcc.getId());
        }
        return Boolean.TRUE;
    }


    /**
     * 广播交易
     *
     * @param withdrawBroadcastRequest
     */
    @Override
    public Boolean withdrawBroadcast(WithdrawBroadcastRequest withdrawBroadcastRequest) {
        if (StringUtils.isBlank(withdrawBroadcastRequest.getSignedData()) || StringUtils.isBlank(withdrawBroadcastRequest.getTradeNo())) {
            throw new CommonException(DServiceConstant.PARAMS_ERROR.getCode(), DServiceConstant.PARAMS_ERROR.getMsg());
        }
        WithdrawOrderRecordBean withdrawOrderRecordBean = withdrawOrderRecordBeanMapper.findByTradeNo(withdrawBroadcastRequest.getTradeNo());
        if (withdrawOrderRecordBean == null) {
            throw new CommonException(DServiceConstant.PARAMS_ERROR.getCode(), DServiceConstant.PARAMS_ERROR.getMsg());
        }
        BroadcastTransactionBean broadcastTransactionBean = broadcastTransactionBeanMapper.findByRefIdAndType(withdrawOrderRecordBean.getId().intValue(), "WITHDRAW");

        if (broadcastTransactionBean == null) {
            throw new CommonException(DServiceConstant.PARAMS_ERROR.getCode(), DServiceConstant.PARAMS_ERROR.getMsg());
        }
        //0 初始化；1 未广播交易；2 广播交易成功 ；3 广播交易失败;4 已达到足够确认数
        broadcastTransactionBeanMapper.updateById(broadcastTransactionBean.getId(), withdrawBroadcastRequest.getSignedData(), null, 1);
        Web3j web3j = null;
        if (withdrawOrderRecordBean.getCurrency().equals("ETH")) {
            web3j = ethClient;
        } else if (withdrawOrderRecordBean.getCurrency().equals("ETC")) {
            web3j = etcClient;
        }
        // todo list  节点广播交易用异步
        try {
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(withdrawBroadcastRequest.getSignedData()).send();
            logger.info("提币广播交易返回值 withdrawBroadcastRequest ={},txHash={}", withdrawBroadcastRequest, ethSendTransaction != null ? ethSendTransaction.getTransactionHash() : null);
            if (ethSendTransaction != null && ethSendTransaction.getTransactionHash() != null) {
                //0 初始化；1 未广播交易；2 广播交易成功 ；3 广播交易失败;4 已达到足够确认数
                broadcastTransactionBeanMapper.updateById(broadcastTransactionBean.getId(), withdrawBroadcastRequest.getSignedData(), ethSendTransaction.getTransactionHash(), 2);
                return Boolean.TRUE;
            } else {
                logger.info("提币广播交易失败 withdrawBroadcastRequest ={},error={}", withdrawBroadcastRequest, ethSendTransaction.getError().getMessage());
            }
        } catch (Exception ex) {
            logger.error("提币广播交易失败，withdrawBroadcastRequest ={},error={}", withdrawBroadcastRequest, ex);
        }
        return Boolean.FALSE;
    }

    /**
     * 插入日志
     *
     * @param orderId
     * @param currency
     * @param parentCurrency
     * @param operatorId
     * @param remark
     * @param withdrawOrderRecordStatusEnum
     */
    private void insertLog(BigInteger orderId, String currency, String parentCurrency, String operatorId,
                           String remark, WithdrawOrderRecordStatusEnum withdrawOrderRecordStatusEnum) {
        WithdrawOrderLog withdrawOrderLog = new WithdrawOrderLog();
        withdrawOrderLog.setOrderId(orderId);
        withdrawOrderLog.setCurrency(currency);
        withdrawOrderLog.setParentCurrency(parentCurrency);
        withdrawOrderLog.setEvent(withdrawOrderRecordStatusEnum.getName());
        withdrawOrderLog.setOperatorId(operatorId == null ? "0" : operatorId);
        withdrawOrderLog.setRemark(remark);
        withdrawOrderLogMapper.insert(withdrawOrderLog);
    }

    @Override
    @Transactional
    public void updateStatusByRefNo(WithdrawMessageResponse withdrawMessageResponse) {
        WithdrawOrderRecordBean withdrawOrderRecord = withdrawOrderRecordBeanMapper.findByTradeNo(withdrawMessageResponse.getRefNo());
        if (withdrawOrderRecord == null) {
            logger.info("updateStatusByRefNo error, withdrawOrderRecord is null,withdrawMessageResponse-> {}", withdrawMessageResponse);
            return;
        }

        if (!withdrawOrderRecord.getStatus().equals(WithdrawOrderRecordStatusEnum.PAYING.name())) {
            logger.info("updateStatusByRefNo , withdrawOrderRecord status is invalid,withdrawMessageResponse-> {}",
                    withdrawMessageResponse);
            return;
        }

        WithdrawMessageResponse.StatusEnum statusEnum = WithdrawMessageResponse.StatusEnum.valueof(withdrawMessageResponse.getStatus());
        WithdrawOrderRecordStatusEnum withdrawOrderRecordStatusEnum = WithdrawOrderRecordStatusEnum.valueOf(statusEnum.name());

        if (statusEnum.equals(WithdrawMessageResponse.StatusEnum.AUTO_CONFIRM)) {
            withdrawOrderRecordStatusEnum = WithdrawOrderRecordStatusEnum.PAYING;
        }

        if (withdrawOrderRecordStatusEnum.equals(WithdrawOrderRecordStatusEnum.SUCCESS)) {
            this.withdrawStatusCallBack(withdrawOrderRecord.getTradeNo(), Boolean.TRUE);
        }
        if (withdrawOrderRecordStatusEnum.equals(WithdrawOrderRecordStatusEnum.FAILURE)) {
            this.withdrawStatusCallBack(withdrawOrderRecord.getTradeNo(), Boolean.FALSE);
        }

        withdrawOrderRecordBeanMapper.updateStatusAndTx(BigInteger.valueOf(withdrawOrderRecord.getId()), withdrawOrderRecordStatusEnum,
                withdrawMessageResponse.getTxId(), withdrawMessageResponse.getAddressFrom());

        this.insertLog(BigInteger.valueOf(withdrawOrderRecord.getId()), withdrawOrderRecord.getCurrency(), withdrawOrderRecord.getParentCurrency(),
                null, String.format("%s->%s,withdrawMessageResponse->%s",
                        withdrawOrderRecord.getStatus(), withdrawOrderRecordStatusEnum.getName(), withdrawMessageResponse), WithdrawOrderRecordStatusEnum.PAYING);
    }
}