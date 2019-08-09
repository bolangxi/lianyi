package com.ted.resonance.scheduler;

import com.ted.resonance.config.DataDictCache;
import com.ted.resonance.config.DataDictConstant;
import com.ted.resonance.entity.BroadcastTransactionBean;
import com.ted.resonance.entity.WithdrawOrderRecordBean;
import com.ted.resonance.mapper.BroadcastTransactionBeanMapper;
import com.ted.resonance.mapper.WithdrawOrderRecordBeanMapper;
import com.ted.resonance.entity.request.WithdrawBroadcastRequest;
import com.ted.resonance.entity.response.WithdrawMessageResponse;
import com.ted.resonance.service.WithdrawOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

/**
 * 提币 广播交易与确认足够区块确认数
 */
@Component
public class WithdrawScheduler implements Serializable {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BroadcastTransactionBeanMapper broadcastTransactionBeanMapper;

    @Autowired
    @Qualifier("ethClient")
    private Web3j ethClient;

    @Autowired
    @Qualifier("etcClient")
    private Web3j etcClient;

    @Autowired
    private WithdrawOrderService withdrawOrderService;

    @Autowired
    private WithdrawOrderRecordBeanMapper withdrawOrderRecordBeanMapper;

    /**
     * 提币 确认
     */
    @Scheduled(cron = "${cron.withdraw.confirm}")
    public void confirm() {
        Integer skip = 0;
        Integer num = 20;
        while (true) {
            List<BroadcastTransactionBean> broadcastTransactionBeanList = broadcastTransactionBeanMapper.findUnconfirm(skip, num);
            skip += num;
            if (CollectionUtils.isEmpty(broadcastTransactionBeanList)) {
                break;
            }

            broadcastTransactionBeanList.forEach(p -> {
                logger.info("提币确认开始，broadcastTransactionBean={}", p);
                try {
                    Web3j web3j = null;
                    BigInteger confirmationNum = null;

                    if (p.getCurrency().equals("ETH")) {
                        web3j = ethClient;
                        confirmationNum = BigInteger.valueOf(Long.valueOf(DataDictCache.getDataValue(DataDictConstant.WITHDRAW_ETH_CONFIRM)));
                    } else if (p.getCurrency().equals("ETC")) {
                        web3j = etcClient;
                        confirmationNum = BigInteger.valueOf(Long.valueOf(DataDictCache.getDataValue(DataDictConstant.WITHDRAW_ETC_CONFIRM)));
                    }
                    logger.info("提币确认，broadcastTransactionBean={},推荐确认数={}", p, confirmationNum);

                    EthGetTransactionReceipt ethGetTransactionReceipt = web3j.ethGetTransactionReceipt(p.getTxHash()).send();
                    TransactionReceipt transactionReceipt = ethGetTransactionReceipt.getTransactionReceipt().get();

                    BigInteger blockNumber = transactionReceipt.getBlockNumber();
                    logger.info("提币确认，broadcastTransactionBean={},当前交易区块高度={}", p, blockNumber);
                    BigInteger latestBlockNumber = web3j.ethBlockNumber().send().getBlockNumber();
                    logger.info("提币确认，broadcastTransactionBean={},最新区块高度={}", p, latestBlockNumber);

                    BigInteger currentConfirmations = latestBlockNumber.subtract(blockNumber);
                    logger.info("提币确认，broadcastTransactionBean={},区块差值={}", p, currentConfirmations);
                    Integer status = null;
                    if (p.getCurrency().equals("ETH")) {
                        status = Integer.valueOf(transactionReceipt.getStatus()
                                .replace("0x", ""), 16) == 1 ? 1 : 0;
                    } else if (p.getCurrency().equals("ETC")) {
                        status = CollectionUtils.isEmpty(transactionReceipt.getLogs()) ? 0 : 1;
                    }

                    logger.info("提币确认，broadcastTransactionBean={},交易状态={}", p, status);

                    if (currentConfirmations.compareTo(confirmationNum) >= 0) {
                        WithdrawOrderRecordBean withdrawOrderRecordBean = withdrawOrderRecordBeanMapper.findById(BigInteger.valueOf(p.getRefId()));
                        WithdrawMessageResponse withdrawMessageResponse = new WithdrawMessageResponse();
                        withdrawMessageResponse.setAddressFrom(transactionReceipt.getFrom());
                        withdrawMessageResponse.setRefNo(withdrawOrderRecordBean.getTradeNo());
                        withdrawMessageResponse.setStatus(status.equals(1) ? WithdrawMessageResponse.StatusEnum.SUCCESS.getIndex() : WithdrawMessageResponse.StatusEnum.FAILURE.getIndex());
                        withdrawMessageResponse.setTxId(p.getTxHash());
                        withdrawOrderService.updateStatusByRefNo(withdrawMessageResponse);
                        //0 初始化；1 未广播交易；2 广播交易成功 ；3 广播交易失败;4 已达到足够确认数
                        broadcastTransactionBeanMapper.updateById(p.getId(), null, null, 4);
                    }
                } catch (Exception ex) {
                    logger.error("提币确认出错，broadcastTransactionBean={},ex={}", p, ex);
                }
            });
        }
    }


    /**
     * 提币 广播交易
     */
    @Scheduled(cron = "${cron.withdraw.broadcast}")
    public void broadcast() {
        Integer skip = 0;
        Integer num = 20;
        while (true) {
            List<BroadcastTransactionBean> broadcastTransactionBeanList = broadcastTransactionBeanMapper.findBroadcast(skip, num);
            skip += num;
            if (CollectionUtils.isEmpty(broadcastTransactionBeanList)) {
                break;
            }

            broadcastTransactionBeanList.forEach(p -> {
                logger.info("广播交易开始，broadcastTransactionBean={}", p);
                try {
                    WithdrawOrderRecordBean withdrawOrderRecordBean = withdrawOrderRecordBeanMapper.findById(BigInteger.valueOf(p.getRefId()));
                    WithdrawBroadcastRequest withdrawBroadcastRequest = new WithdrawBroadcastRequest();
                    withdrawBroadcastRequest.setSignedData(p.getSignedData());
                    withdrawBroadcastRequest.setTradeNo(withdrawOrderRecordBean.getTradeNo());
                    withdrawOrderService.withdrawBroadcast(withdrawBroadcastRequest);
                } catch (Exception ex) {
                    logger.error("广播交易出错，broadcastTransactionBean={},ex={}", p, ex);
                }
            });
        }
    }
}
