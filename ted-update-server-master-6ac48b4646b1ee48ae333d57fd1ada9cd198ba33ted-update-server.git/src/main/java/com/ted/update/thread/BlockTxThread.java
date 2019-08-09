package com.ted.update.thread;

import com.ted.update.entity.Tx;
import com.ted.update.enums.TokenType;
import com.ted.update.enums.TxType;
import com.ted.update.util.DataUtil;
import com.ted.update.util.TedLogger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by qingyun.yu on 2018/11/24.
 */
@Component
public class BlockTxThread implements TedLogger {
    private Long blockNumber;
    private EthBlock.Block block;
    private List<Tx> blockTxList = Collections.synchronizedList(new ArrayList<>());
    @Autowired
    private Web3j web3j;
    @Value("${blockchain.asset}")
    private String blockchainAsset;
    private CountDownLatch countDownLatch;
    private final static Integer poolSize = 10;
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(poolSize, poolSize, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(poolSize), Executors.defaultThreadFactory());

    protected LinkedBlockingQueue<EthBlock.TransactionResult<EthBlock.TransactionObject>> txQueue = new LinkedBlockingQueue<>();

    public void execute() {
        try {
            block = web3j.ethGetBlockByNumber(new DefaultBlockParameterNumber(blockNumber), true).send().getBlock();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.countDownLatch = new CountDownLatch(block.getTransactions().size());
        block.getTransactions().forEach(tx -> {
            try {
                txQueue.put(tx);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }


    @PostConstruct
    public void init() {
        for (int i = 0; i < poolSize; i++) {
            threadPoolExecutor.execute(() -> {
                for (; ; ) {
                    try {
                        EthBlock.TransactionObject transaction = txQueue.take().get();
                        EthGetTransactionReceipt transactionReceipt = web3j.ethGetTransactionReceipt(transaction.getHash()).send();
                        if (this.verifyTokenTransaction(transaction)) {
                            TokenType tokenType = TokenType.tokenType(transaction.getTo());
                            TxType txType = TxType.txType(transaction.getInput().substring(0, 10));
                            String fromAddr = null;
                            String toAddr = null;
                            BigDecimal ethAmount = DataUtil.formatAmount(new BigDecimal(transaction.getValue()), 18);
                            BigDecimal tedAmount = BigDecimal.ZERO;
                            String type = "";
                            List<Type> deDate = txType.parse(transactionReceipt);
                            switch (txType) {
                                case TRANSFER:
                                case BURN:
                                case EXCHANGE: {
                                    type = txType.name();
                                    fromAddr = deDate.get(0).toString();
                                    toAddr = deDate.get(1).toString();
                                    String value = deDate.get(2).getValue().toString();
                                    tedAmount = DataUtil.formatAmount(new BigDecimal(value), tokenType.decimals());
                                }
                            }

                            if (ethAmount.compareTo(BigDecimal.ZERO) >= 0 && tedAmount.compareTo(BigDecimal.ZERO) >= 0 && Strings.isNotBlank(fromAddr) && Strings.isNotBlank(toAddr)) {
                                Tx tx = new Tx();
                                tx.setHash(transaction.getHash());
                                tx.setBlockHash(block.getHash());
                                tx.setBlockHeight(block.getNumber().intValue());
                                tx.setFromAddr(fromAddr);
                                tx.setToAddr(toAddr);
                                tx.setTimestamp(block.getTimestamp().longValue());
                                tx.setEthAmount(ethAmount);
                                tx.setTedAmount(tedAmount);
                                tx.setAssetShortName(tokenType.tokenName());
                                tx.setMainCoin(blockchainAsset);
                                tx.setFee(DataUtil.formatAmount(transaction.getGas().multiply(transaction.getGasPrice()).toString(16), 18));
                                tx.setGasPrice(new BigDecimal(transaction.getGasPrice()));
                                tx.setGasLimit(new BigDecimal(transaction.getGas()));
                                tx.setTxNum(0);
                                tx.setTxId(tx.getHash());
                                tx.setState(Integer.valueOf(transactionReceipt
                                        .getTransactionReceipt().get()
                                        .getStatus()
                                        .replace("0x", ""), 16) == 1 ? 1 : 0);
                                tx.setContractAddr(transaction.getTo());
                                tx.setType(type);
                                tx.setBlockState((short) 1);
                                tx.setComment("");
                                tx.setNonce(transaction.getNonce().intValue());
                                blockTxList.add(tx);
                            }
                        }
                    } catch (Exception e) {
                        logger.error(e.toString());
                        e.printStackTrace();
                    } finally {
                        this.countDownLatch.countDown();
                    }
                }
            });
        }
    }

    public List<Tx> getTxList() {
        return blockTxList;
    }

    public void await() {
        try {
            this.countDownLatch.await();
        } catch (Exception e) {
            logger.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    public void init(Long blockNumber) {
        this.blockNumber = blockNumber;
        blockTxList = Collections.synchronizedList(new ArrayList<>());
    }

    private Boolean verifyTokenTransaction(EthBlock.TransactionObject transaction) {
        return Objects.nonNull(transaction)
                && Strings.isNotBlank(transaction.getHash())
                && Strings.isNotBlank(transaction.getTo())
                && Objects.nonNull(TokenType.tokenType(transaction.getTo()))
                && Objects.nonNull(transaction.getInput())
                && Objects.nonNull(TxType.txType(transaction.getInput().substring(0, 10)));
    }
}
