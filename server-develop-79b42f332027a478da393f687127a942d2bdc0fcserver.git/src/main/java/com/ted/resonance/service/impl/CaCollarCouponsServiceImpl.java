package com.ted.resonance.service.impl;

import com.ted.resonance.config.DServiceConstant;
import com.ted.resonance.config.DataDictCache;
import com.ted.resonance.dto.OperateUserAccDto;
import com.ted.resonance.entity.CaCollarCouponsBean;
import com.ted.resonance.entity.UserAccBean;
import com.ted.resonance.entity.request.BalanceRequest;
import com.ted.resonance.entity.request.PayCouponsRequest;
import com.ted.resonance.entity.TxBean;
import com.ted.resonance.entity.response.BalanceResponse;
import com.ted.resonance.enums.*;
import com.ted.resonance.mapper.CaCollarCouponsBeanMapper;
import com.ted.resonance.mapper.TxBeanMapper;
import com.ted.resonance.mapper.UserAccBeanMapper;
import com.ted.resonance.service.CaCollarCouponsService;
import com.ted.resonance.service.UserAccService;
import com.ted.resonance.utils.DateUtil;
import com.ted.resonance.utils.exceptions.CommonException;
import com.ted.resonance.utils.exceptions.SendTransactionException;
import com.ted.resonance.utils.web.ResponseEntity;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

import java.io.IOException;
import java.util.*;

@Service
public class CaCollarCouponsServiceImpl implements CaCollarCouponsService {
    @Autowired
    private CaCollarCouponsBeanMapper caCollarCouponsRepo;
    @Autowired
    private TxBeanMapper txBeanMapper;
    @Autowired
    private UserAccService userAccService;
    @Autowired
    private UserAccBeanMapper userAccMapper;

    @Autowired
    private CaCollarCouponsService caCollarCouponsService;


    @Autowired
    @Qualifier("ethClient")
    private Web3j ethClient;


    private static Logger logger = LoggerFactory.getLogger(CaCollarCouponsServiceImpl.class);

    @Transactional
    @Override
    public int updateCollarCoupons(CaCollarCouponsBean caCollarCoupons) {

        if (caCollarCoupons == null || StringUtils.isEmpty(caCollarCoupons.getAddr())) {
            throw new CommonException(DServiceConstant.PARAMS_ERROR.getCode(), DServiceConstant.PARAMS_ERROR.getMsg());
        }

        //通过用户Id查询当前的用户领金券信息
        CaCollarCouponsBean caCollarCouponsBean = caCollarCouponsRepo.findCaCollarCouponsByUserId(caCollarCoupons.getAddr());
        if (caCollarCouponsBean == null) {
            throw new CommonException(DServiceConstant.PARAMS_ERROR.getCode(), DServiceConstant.PARAMS_ERROR.getMsg());
            //数据不存在
        }
        Date rewardTime = new Date();
        Date expiresTime = DateUtil.getAfterHour(rewardTime, 24);
        String rewardStatus = "2"; //已领取
        int num = caCollarCouponsRepo.updateRewardStatus(caCollarCouponsBean.getId(), rewardStatus, rewardTime, expiresTime);
        if (num > 0) {
            //数据更新成功
            return num;
        }
        return 0;
    }

    @Override
    public CaCollarCouponsBean findCaCollarCouponsByUserId(String userId) {
        return caCollarCouponsRepo.findCaCollarCouponsByUserId(userId);


    }

    /**
     * 根据地址  获取用户的领金券列表
     *
     * @param addr
     * @return
     */
    @Override
    public ResponseEntity<List<CaCollarCouponsBean>> couponsList(String addr) {
        ResponseEntity<List<CaCollarCouponsBean>> responseEntity = new ResponseEntity<>();
        List<CaCollarCouponsBean> caCollarCouponsBeans = caCollarCouponsRepo.couponsList(addr);
        responseEntity.setContent(caCollarCouponsBeans);
        return responseEntity;
    }

    @Transactional
    @Override
    public ResponseEntity<CaCollarCouponsBean> payCoupons(PayCouponsRequest payCouponsRequest) {
        ResponseEntity<CaCollarCouponsBean> responseEntity = new ResponseEntity();
        if (payCouponsRequest == null) {
            throw new CommonException(DServiceConstant.PARAMS_ERROR.getCode(), DServiceConstant.PARAMS_ERROR.getMsg());
        }
        Long id = payCouponsRequest.getCouponsId();
        String addr = payCouponsRequest.getAddr();
        if (id == 0L || StringUtils.isEmpty(addr)) {
            throw new CommonException(DServiceConstant.PARAMS_ERROR.getCode(), DServiceConstant.PARAMS_ERROR.getMsg());
        }
        //根据领金券Id 查询领金券信息
        CaCollarCouponsBean caCollarCouponsBean = caCollarCouponsRepo.findCouponsById(id);
        if (caCollarCouponsBean == null) {
            throw new CommonException(DServiceConstant.COUPONS_NOT_EXIST.getCode(), DServiceConstant.COUPONS_NOT_EXIST.getMsg());
        }
        //更新该收益卡状态为使用中
        int num = caCollarCouponsRepo.updateRewardStatus(id, "3", caCollarCouponsBean.getExpiresTime(), caCollarCouponsBean.getRewardTime());
        if (num > 0) {
            EthSendTransaction ethSendTransaction = null;
            try {
                ethClient.ethSendRawTransaction(payCouponsRequest.getSignedApproveData()).send();
                ethSendTransaction = ethClient.ethSendRawTransaction(payCouponsRequest.getSignedCouponsData()).send();
            } catch (IOException e) {
                throw new SendTransactionException(999, "send pay coupons failed");
            }

            if (Objects.isNull(ethSendTransaction)) {
                throw new SendTransactionException(999, "send pay coupons  failed");
            } else if (Strings.isBlank(ethSendTransaction.getTransactionHash())) {
                throw new SendTransactionException(999, ethSendTransaction.getError().getMessage());

            }
        }
        return null;
    }


    @Override
    @Transactional
    public void calCouponIncome() {
        List<CaCollarCouponsBean> beanList = caCollarCouponsRepo.selectCouponsByRewardStatus(RewardStatusEnum.USEING.getCode());
        if (!CollectionUtils.isEmpty(beanList)) {
            logger.info("calCouponIncome beanList.size={}", beanList.size());
            beanList.forEach(couponsBean -> {
                this.handleUserCouponsBean(couponsBean);
            });

        }
    }

    /**
     * 查询余额
     *
     * @param balanceRequest
     * @return
     */
    @Override
    public List<BalanceResponse> selectBalances(BalanceRequest balanceRequest) {
        List<UserAccBean> list = userAccMapper.selectBalances(balanceRequest);
        List<BalanceResponse> result = new ArrayList<>();
        for (UserAccBean bean : list) {
            BalanceResponse response = new BalanceResponse();
            response.setAddr(bean.getAddr());
            response.setBalance(bean.getBalance());
            response.setCoinType(bean.getCoinType());
            result.add(response);
        }
        return result;
    }

    @Override
    public Map<String, Object> withDrawBalance(BalanceRequest balanceRequest) {
        List<BalanceResponse> balanceResponseList = caCollarCouponsService.selectBalances(balanceRequest);
        Map<String, Object> map = new HashMap<>();
        if (balanceResponseList != null && balanceResponseList.size() > 0) {
            map.put("balanceResponseList", balanceResponseList);
            map.put("balance", balanceResponseList.get(0).getBalance());
            if (balanceRequest != null && null != balanceRequest.getCoinType() && !"".equals(balanceRequest.getCoinType())) {
                if ("ETH".equals(balanceRequest.getCoinType())) {
                    map.put("rate", DataDictCache.getDataValue("ethPoundage"));
                } else if ("ETC".equals(balanceRequest.getCoinType())) {
                    map.put("rate", DataDictCache.getDataValue("etcPoundage"));
                } else {
                    map.put("rate", 0);
                }
            }
            return map;
        }
        return null;
    }

    private void handleUserCouponsBean(CaCollarCouponsBean couponsBean) {
        String txHash = couponsBean.getTxHash();
        if (!StringUtils.isEmpty(txHash)) {
            TxBean txBean = txBeanMapper.selectByTxHash(txHash);
            if (txBean != null) {
                if (StatusEnum.FAIL.getCode() == txBean.getState()) {
                    this.handleFailCoupons(couponsBean);
                }
                //链上执行成功
                if (StatusEnum.SUCCESS.getCode() == txBean.getState()) {
                    //链上确认失败
                    if (ConfirmEnum.FAIL.getCode() == txBean.getState()) {
                        this.handleFailCoupons(couponsBean);
                    }
                    //链上确认成功
                    if (ConfirmEnum.SUCCESS.getCode() == txBean.getState()) {
                        Boolean judgeResult = this.judgeTxPayAmount(txBean, couponsBean);
                        if (!judgeResult) {
                            caCollarCouponsRepo.updateDelStatusById(couponsBean.getId());
                            return;
                        }
                        OperateUserAccDto userAccDto = new OperateUserAccDto();
                        userAccDto.setUserId(couponsBean.getId());
                        userAccDto.setAddr(couponsBean.getAddr());
                        userAccDto.setCoinType(couponsBean.getRewardCoinType());
                        userAccDto.setTradeAmount(couponsBean.getRewardCoinNums());
                        userAccDto.setOperateType(OperateType.ADD);
                        userAccDto.setTradeType(TradeType.CA_REWARD);
                        //金额入账
                      Long result = userAccService.operateUserAccByCoin(userAccDto);
                      if(result>0){
                          caCollarCouponsRepo.updateRewardStatusById(couponsBean.getId(), RewardStatusEnum.USED.getCode());
                      }

                    }

                }
            }

        }
    }

    private Boolean judgeTxPayAmount(TxBean txBean, CaCollarCouponsBean couponsBean) {
        if (txBean != null && couponsBean != null) {
            if (txBean.getTedAmount() != null && txBean.getEthAmount() != null) {
                if (couponsBean.getPayTedNums() != null && couponsBean.getPayCoinNums() != null) {
                    if (couponsBean.getPayTedNums().compareTo(txBean.getTedAmount()) == 0 && couponsBean.getPayCoinNums().compareTo(txBean.getEthAmount()) == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * 链上失败领金券处理
     *
     * @param couponsBean
     */
    private void handleFailCoupons(CaCollarCouponsBean couponsBean) {
        CaCollarCouponsBean collarCouponsBean = caCollarCouponsRepo.selectById(couponsBean.getId());
        if (collarCouponsBean.getExpiresTime().compareTo(new Date()) <= 0) {
            caCollarCouponsRepo.updateDelStatusById(couponsBean.getId());
        } else {
            caCollarCouponsRepo.updateRewardStatusById(couponsBean.getId(), RewardStatusEnum.RECEIVED.getCode());
        }
    }


}
