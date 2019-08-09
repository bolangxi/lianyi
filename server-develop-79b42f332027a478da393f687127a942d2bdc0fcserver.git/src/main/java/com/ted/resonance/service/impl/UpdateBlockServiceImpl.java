package com.ted.resonance.service.impl;

import com.ted.resonance.entity.AddressInfo;
import com.ted.resonance.entity.BlockInfo;
import com.ted.resonance.repository.BlockInfoRepo;
import com.ted.resonance.repository.TransactionRepo;
import com.ted.resonance.service.TransactionService;
import com.ted.resonance.service.UpdateBlockService;
import com.ted.resonance.utils.ContractUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Service
public class UpdateBlockServiceImpl implements UpdateBlockService {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateBlockService.class);

    @Autowired @Qualifier("etcClient")
    private Web3j etcClient;

    @Autowired @Qualifier("ethClient")
    private Web3j ethClient;

    @Autowired
    private BlockInfoRepo blockInfoRepo;

    @Autowired private TransactionRepo transactionRepo;

    @Autowired private TransactionService transactionService;

    @Value("${etcMinHeight}") private Integer etcMinHeight;

    @Value("${ethMinHeight}") private Integer ethMinHeight;

    @Value("${ethContractAddress}") private String ethContractAddress;
    @Value("${etcWorkerPrivateKey}") private String workerPrivateKey;


    @Override
    @Scheduled(fixedDelay = 30000)
    public void updateBlock() {
        LOG.info("updating data from etc ...");
        while(true) {
            try {
                BlockInfo blockInfo = blockInfoRepo.findTopByTypeOrderByHeightDesc("etc");
                BlockInfo newBlockInfo = new BlockInfo();
                newBlockInfo.setType("etc");
                //从数据库获取区块高度， 没有则用配置的最小高度， 超过直接返回
                if(blockInfo == null) {
                    LOG.info("no previous etc block");
                    newBlockInfo.setHeight(etcMinHeight);
                    newBlockInfo.setPrevious("0");
                }else {
                    newBlockInfo.setPrevious(blockInfo.getHash());
                    newBlockInfo.setHeight(Math.max(etcMinHeight, blockInfo.getHeight() + 1));
                }
                if(newBlockInfo.getHeight() > transactionService.getEtcCurrentBlock().intValue()) {
                    break;
                }

                List<com.ted.resonance.entity.Transaction> pendingTransactions = transactionRepo.findAllByStatusAndType(0, "etc");
                //遍历该高度的区块
                EthBlock block = etcClient.ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(newBlockInfo.getHeight())), true).send();
                for(EthBlock.TransactionResult<Transaction> transactionResult : block.getBlock().getTransactions()){
                    for(com.ted.resonance.entity.Transaction pendingTransation : pendingTransactions) {
                        if (pendingTransation.getTransactionHash() != null && pendingTransation.getTransactionHash().equals(transactionResult.get().getHash())) {
                            TransactionReceipt transactionReceipt = etcClient.ethGetTransactionReceipt(transactionResult.get().getHash()).send().getResult();
                            if(transactionReceipt.getStatus().equals("0x1")) {
                                //交易成功
                                handleTransaction(transactionReceipt, pendingTransation, block.getBlock().getNumber().intValue());

                            }else {
                                //交易失败
                                if(pendingTransation.getPayment().compareTo(BigDecimal.valueOf(0)) == 0) {
                                    //领奖的交易
                                    LOG.info("user get reward failed");
                                    transactionService.rewardTransactionFail(pendingTransation);
                                }else {
                                    transactionService.transactionFail(pendingTransation, block.getBlock().getNumber().intValue());
                                }
                            }
                        }
                    }
                }
                newBlockInfo.setSize(block.getBlock().getSize().intValue());
                newBlockInfo.setHash(block.getBlock().getHash());
                newBlockInfo.setTimestamp(block.getBlock().getTimestamp().intValue());
                blockInfoRepo.save(newBlockInfo);

            }catch (Exception e) {
                e.printStackTrace();
                LOG.error(e.getMessage());
                return;
            }
        }
        LOG.info("updating data from eth ...");
        while (true) {
            try {
                BlockInfo blockInfo = blockInfoRepo.findTopByTypeOrderByHeightDesc("eth");
                BlockInfo newBlockInfo = new BlockInfo();
                newBlockInfo.setType("eth");
                if(blockInfo == null) {
                    newBlockInfo.setHeight(ethMinHeight);
                    newBlockInfo.setPrevious("0");
                }else {
                    newBlockInfo.setPrevious(blockInfo.getHash());
                    newBlockInfo.setHeight(Math.max(etcMinHeight, blockInfo.getHeight() + 1));
                }
                if(newBlockInfo.getHeight() > transactionService.getEthCurrentBlock().intValue()) {
                    return;
                }

                List<com.ted.resonance.entity.Transaction> pendingTransactions = transactionRepo.findAllByStatusAndType(0, "eth");
                //eth to ted 交易
                pendingTransactions.addAll(transactionRepo.findAllByStatusAndType(0, "ted"));
                //遍历该高度的区块
                EthBlock block = ethClient.ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(newBlockInfo.getHeight())), true).send();
                for(EthBlock.TransactionResult<Transaction> transactionResult : block.getBlock().getTransactions()){
                    for(com.ted.resonance.entity.Transaction pendingTransation : pendingTransactions) {
                        if (pendingTransation.getTransactionHash() !=null && pendingTransation.getTransactionHash().equals(transactionResult.get().getHash())) {
                            //交易成功逻辑
                            TransactionReceipt transactionReceipt = ethClient.ethGetTransactionReceipt(transactionResult.get().getHash()).send().getResult();
                            if(transactionReceipt.getStatus().equals("0x1")) {
//                                transactionService.transactionSuccess(pendingTransation, block.getBlock().getNumber().intValue());
                                //给用户在etc链打ted
                                handleTransaction(transactionReceipt, pendingTransation, block.getBlock().getNumber().intValue());
                            }else {
                                //交易失败
                                if(pendingTransation.getPayment().compareTo(BigDecimal.valueOf(0)) == 0) {
                                    //领奖的交易
                                    LOG.info("user get reward failed");
                                    transactionService.rewardTransactionFail(pendingTransation);
                                }else {
                                    transactionService.transactionFail(pendingTransation, block.getBlock().getNumber().intValue());
                                }                            }
                        }
                    }
                }
                newBlockInfo.setHash(block.getBlock().getHash());
                newBlockInfo.setSize(block.getBlock().getSize().intValue());
                newBlockInfo.setTimestamp(block.getBlock().getTimestamp().intValue());
                blockInfoRepo.save(newBlockInfo);

            }catch (Exception e) {
                e.printStackTrace();
                LOG.error(e.getMessage());
            }
        }
    }

    @Override
    public BigInteger getEtcNonce(String address) throws Exception{
        return etcClient.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).send().getTransactionCount();
    }

    @Override
    public BigInteger getEthNonce(String address) throws Exception{
        return ethClient.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).send().getTransactionCount();
    }

    private void handleTransaction(TransactionReceipt transactionReceipt, com.ted.resonance.entity.Transaction transaction, Integer blockNumber) throws Exception{
        LOG.info("handleTransation ...");
        if(transaction.getPayment().compareTo(BigDecimal.valueOf(0)) == 0) {
            //领奖的交易
            LOG.info("user get reward success");
            transaction.setStatus(1);
            transactionRepo.save(transaction);
            return;
        }
        for(Log log : transactionReceipt.getLogs()) {
            String topic = log.getTopics().get(0);
            Event exchangeEvent = ContractUtils.exchangeEvent();
            Event couponEvent = ContractUtils.couponEvent();
            String exchangeSig = EventEncoder.encode(exchangeEvent);
            String couponSig = EventEncoder.encode(couponEvent);
            LOG.info(topic);
            LOG.info(couponSig);
            List<Type> nonIndexedValues = null;
            if(topic.equals(exchangeSig)) {
                //exchage log
                LOG.info(" exchage method");
                nonIndexedValues = FunctionReturnDecoder.decode(log.getData(), exchangeEvent.getNonIndexedParameters());
            }else if(topic.equals(couponSig)){
                //coupon log
                LOG.info(" coupon method");
                nonIndexedValues = FunctionReturnDecoder.decode(log.getData(), couponEvent.getNonIndexedParameters());

            }
            if(nonIndexedValues !=null) {
                BigInteger eth = ((Uint256)nonIndexedValues.get(0)).getValue();
                BigInteger ted = ((Uint256)nonIndexedValues.get(1)).getValue();
                if(transaction.getType().equals("etc")) {
                    sendEtcTed(transactionReceipt.getFrom(), transactionReceipt.getTransactionHash(), eth, ted, transaction.getPhase());
                }
                LOG.info("coin type: " + transaction.getType());
                LOG.info("got eth: " + new BigDecimal(eth, 18));
                LOG.info("send ted: " + new BigDecimal(ted, 18));
                transaction.setReward(new BigDecimal(ted, 18));
                transaction.setPayment(new BigDecimal(eth, 18));
            }
        }
        transaction.setStatus(1);
        if(transaction.getType().equals("ted")){
            transactionRepo.save(transaction);
        }else {
            transactionService.transactionSuccess(transaction, blockNumber);
        }
    }

    /**
     * 用户在etc链上共振， 要在eth链上发放ted
     */
    private void sendEtcTed(String toAddress, String txHash, BigInteger eth, BigInteger ted, Integer phase) throws Exception {
        Credentials credentials = Credentials.create(workerPrivateKey);
        String hash = ContractUtils.ethExchange(ethClient, ethContractAddress, credentials, toAddress, txHash, eth, ted);
        //交易保存到数据库
        com.ted.resonance.entity.Transaction transaction = new com.ted.resonance.entity.Transaction();
        transaction.setTransactionHash(hash);
        transaction.setPhase(phase);
        transaction.setStatus(0);
        transaction.setType("ted");
        transaction.setPayment(new BigDecimal(eth, 18));
        transaction.setReward(new BigDecimal(ted, 18));
        transaction.setAddress(credentials.getAddress());
        transactionService.save(transaction);
    }
}
