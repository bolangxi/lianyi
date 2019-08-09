package com.ted.update.service;


import com.ted.update.entity.Block;
import com.ted.update.entity.SyncError;
import com.ted.update.entity.Tx;

public interface SyncErrorService {
    Integer insertSyncError(SyncError syncError);

    void saveError(Block block, Tx tx, Exception e);
}
