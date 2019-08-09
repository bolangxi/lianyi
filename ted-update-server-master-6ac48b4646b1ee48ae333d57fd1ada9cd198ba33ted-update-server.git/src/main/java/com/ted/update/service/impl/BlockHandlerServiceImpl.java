package com.ted.update.service.impl;

import com.ted.update.entity.Block;
import com.ted.update.entity.Tx;
import com.ted.update.enums.TokenType;
import com.ted.update.service.BlockHandlerService;
import com.ted.update.thread.BlockTxThread;
import com.ted.update.util.DataUtil;
import com.ted.update.util.TedLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.core.methods.response.EthCall;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class BlockHandlerServiceImpl implements BlockHandlerService, TedLogger {
    @Autowired
    private Web3j web3j;
    @Autowired
    private BlockTxThread blockTxThread;
    @Override
    public Integer getBlockNumber() {
        try {
            return web3j.ethBlockNumber().send().getBlockNumber().intValue();
        } catch (IOException e) {
            logger.error("getBlockNumber error: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public List<Block> getBlocks(List<Integer> blockHeights) {
        List<Block> blockHashList = new ArrayList<>();
        blockHeights.forEach(blockHeight -> {
            EthBlock ethBlock = null;
            try {
                ethBlock = web3j.ethGetBlockByNumber(new DefaultBlockParameterNumber(blockHeight), true).send();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Block block = new Block();

            block.setHash(ethBlock.getBlock().getHash());
            block.setHeight(blockHeight);
            block.setPrevious(ethBlock.getBlock().getParentHash());
            block.setSize(ethBlock.getBlock().getSize().intValue());
            block.setTimestamp(ethBlock.getBlock().getTimestamp().longValue());
            block.setTxCount(ethBlock.getBlock().getNumber().intValue());
            block.setState((short)1);
            blockHashList.add(block);
        });
        return blockHashList;
    }

    @Override
    public List<Tx> getBlockTx(Block block) {
        blockTxThread.init(block.getHeight().longValue());
        blockTxThread.execute();
        blockTxThread.await();
        return blockTxThread.getTxList();
    }

    @Override
    public String getBlockHash(Integer blockHeight) {
        try {
            EthBlock ethBlock = web3j.ethGetBlockByNumber(new DefaultBlockParameterNumber(blockHeight), true).send();
            return ethBlock.getBlock().getHash();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer getMemPoolTxCount() {
        return null;
    }

    @Override
    public BigDecimal getAddressBalance(String fromAddress) {
        String methodName = "balanceOf";
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        Address address = new Address(fromAddress);
        inputParameters.add(address);

        TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
        };
        outputParameters.add(typeReference);
        Function function = new Function(methodName, inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddress, TokenType.TED.contractAddress(), data);

        EthCall ethCall;

        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
        BigInteger balanceValue = (BigInteger) results.get(0).getValue();
        return DataUtil.formatAmount(new BigDecimal(balanceValue), TokenType.TED.decimals());
    }
}
