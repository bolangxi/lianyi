package com.ted.resonance.service.impl;

import com.ted.resonance.config.DServiceConstant;
import com.ted.resonance.entity.ExTedBurnBean;
import com.ted.resonance.entity.request.BurnRequest;
import com.ted.resonance.exception.TedException;
import com.ted.resonance.mapper.ExTedBurnBeanMapper;
import com.ted.resonance.service.ExTedBurnService;
import com.ted.resonance.utils.VerifyUtil;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

import java.io.IOException;
import java.util.Objects;

@Service
public class ExTedBurnServiceImpl implements ExTedBurnService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ExTedBurnBeanMapper exTedBurnBeanMapper;
    @Autowired
    @Qualifier("ethClient")
    private Web3j web3j;


    @Override
    public void burnTed(BurnRequest burnRequest) {
        VerifyUtil.stringNotBlank(burnRequest.getAddress(), DServiceConstant.PARAMS_ERROR);
        VerifyUtil.stringNotBlank(burnRequest.getSignedApproveData(), DServiceConstant.PARAMS_ERROR);
        VerifyUtil.stringNotBlank(burnRequest.getSignedBrunData(), DServiceConstant.PARAMS_ERROR);
        VerifyUtil.objectNotNull(burnRequest.getActivityId(), DServiceConstant.PARAMS_ERROR);

        EthSendTransaction ethSendTransaction = null;
        try {
            ethSendTransaction = web3j.ethSendRawTransaction(burnRequest.getSignedApproveData()).send();
            ethSendTransaction = web3j.ethSendRawTransaction(burnRequest.getSignedBrunData()).send();
        } catch (IOException e) {
            logger.error("广播 燃烧ted 交易 {}", e);
            throw new TedException(DServiceConstant.SEND_TX_FAILED, e);
        }
        if (Objects.isNull(ethSendTransaction)) {
            throw new TedException(DServiceConstant.SEND_TX_FAILED, null);
        } else if (Strings.isBlank(ethSendTransaction.getTransactionHash())) {

            logger.error("广播 燃烧ted 交易 {}", ethSendTransaction.getError().getMessage());

            throw new TedException(DServiceConstant.SEND_TX_FAILED, new RuntimeException(ethSendTransaction.getError().getMessage()));
        }

        ExTedBurnBean exTedBurnBean = new ExTedBurnBean();
        exTedBurnBean.setTxHash(ethSendTransaction.getTransactionHash());
        exTedBurnBean.setActivatyId(burnRequest.getActivityId());
        exTedBurnBean.setAmount(burnRequest.getAmount());
        exTedBurnBean.setUserId(Objects.nonNull(burnRequest.getUserId()) ? burnRequest.getUserId() : 0);
        exTedBurnBean.setAddr(burnRequest.getAddress());
        exTedBurnBeanMapper.insertSelective(exTedBurnBean);
    }
}
