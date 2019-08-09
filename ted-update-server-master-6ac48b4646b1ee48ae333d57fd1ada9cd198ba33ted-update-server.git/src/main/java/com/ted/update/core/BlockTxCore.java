package com.ted.update.core;

import com.ted.update.entity.AddressMinBalance;
import com.ted.update.entity.Block;
import com.ted.update.entity.Tx;
import com.ted.update.service.*;
import com.ted.update.util.TedLogger;
import com.ted.update.util.VerifyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class BlockTxCore implements TedLogger {
    @Autowired
    private BlockService blockService;
    @Autowired
    private BlockHandlerService blockHandlerService;
    @Autowired
    private TxService txService;
    @Autowired
    private SyncErrorService syncErrorService;
    @Autowired
    private AddressMinBalanceService addressMinBalanceService;
    @Value("${blockchain.max-rollback-count: 20}")
    private Integer maxRollbackCount;


    public void blockTxHandler() {
        // get unsync block from database
        List<Block> blockList = blockService.selectBlocks();
        Integer rollbackCount = 0;

        // handler block
        blockList.forEach(block -> {
            try {
                logger.info("block:{} handler start", block.getHash());
                nonIsolatedBlock(block.getHeight() - 1, block.getPrevious(), rollbackCount);
                // get tx by block
                List<Tx> txList = blockHandlerService.getBlockTx(block);
                // handler tx
                if (Objects.nonNull(txList)) {
                    for (Tx tx : txList) {
                        try {
                            txService.insertTx(tx);
                            BigDecimal tedBalance = blockHandlerService.getAddressBalance(tx.getFromAddr());
                            if(Objects.nonNull(tedBalance)) {
                                AddressMinBalance addressMinBalance = addressMinBalanceService.selectAddressMinBalance(tx.getFromAddr());
                                if(Objects.nonNull(addressMinBalance) && tedBalance.compareTo(addressMinBalance.getBalance()) < 0) {
                                    addressMinBalanceService.updateBalanceById(tedBalance, addressMinBalance.getId());
                                }
                            }
                        } catch (Exception e) {
                            syncErrorService.saveError(block, tx, e);
                            logger.error("save block:{} tx:{} num: {} failed, exception: {}", block.getHash(), tx.getHash(), tx.getTxNum(), e.getLocalizedMessage());
                        }
                    }
                }
                Integer row = blockService.updateBlockSyncedByHash(block.getHash());
                VerifyUtil.numberNotZore(row, "update block sync failed");

                logger.info("block:{} handler complated", block.getHash());
            } catch (Exception e) {
                syncErrorService.saveError(block, null, e);
                logger.error("save ont block:{} failed, exception: {}", block.getHash(), e.getLocalizedMessage());
            }
        });
    }

    @Transactional
    public void nonIsolatedBlock(Integer blockHeight, String blockHash, Integer count) {
        try {
            if (count >= maxRollbackCount) {
                throw new RuntimeException("回滚超过最大限制" + maxRollbackCount);
            }
            Block parentBlock = blockService.selectBlockByHeight(blockHeight);

            if (Objects.nonNull(parentBlock) && !parentBlock.getHash().equals(blockHash)) {
                logger.warn("handler isolated block:{} start", blockHash);
                txService.deleteTxByBlockHash(parentBlock.getHash());
                blockService.deleteBlockByBlockHash(parentBlock.getHash());
                List<Block> blockList = blockHandlerService.getBlocks(Arrays.asList(blockHeight));
                Integer row = blockService.insertBlock(blockList.get(0));
                VerifyUtil.numberNotZore(row, "回滚用户资产表失败");
                String blockHash_1 = blockHandlerService.getBlockHash(parentBlock.getHeight() - 1);
                nonIsolatedBlock(parentBlock.getHeight() - 1, blockHash_1, ++count);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
