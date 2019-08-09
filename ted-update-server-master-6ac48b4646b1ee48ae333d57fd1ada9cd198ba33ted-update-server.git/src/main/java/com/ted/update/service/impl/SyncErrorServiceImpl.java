package com.ted.update.service.impl;

import com.ted.update.dao.SyncErrorDao;
import com.ted.update.entity.Block;
import com.ted.update.entity.SyncError;
import com.ted.update.entity.Tx;
import com.ted.update.service.SyncErrorService;
import com.ted.update.util.TedLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Service
public class SyncErrorServiceImpl implements SyncErrorService, TedLogger {
    @Autowired
    private SyncErrorDao syncErrorDao;

    @Override
    public Integer insertSyncError(SyncError syncError) {
        return syncErrorDao.insertSyncError(syncError);
    }

    @Override
    @Transactional
    public void saveError(Block block, Tx tx, Exception e) {
        try {
            SyncError syncError = new SyncError();
            syncError.setBlockHash(block.getHash());
            syncError.setBlockHeight(block.getHeight());
            syncError.setErrorInfo(e.getLocalizedMessage());
            if (Objects.nonNull(tx)) {
                syncError.setTxHash(tx.getHash());
                syncError.setTxNum(tx.getTxNum());
            }
            this.insertSyncError(syncError);
        } catch (Exception er) {
            logger.error("save error info failed", er.getLocalizedMessage());
        }
    }
}
