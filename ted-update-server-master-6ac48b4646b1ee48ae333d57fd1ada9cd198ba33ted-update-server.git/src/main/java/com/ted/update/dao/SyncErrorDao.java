package com.ted.update.dao;

import com.ted.update.entity.SyncError;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SyncErrorDao {
    String TABLE_NAME = "sync_error";
    @Insert("insert into " + TABLE_NAME + "(block_height, block_hash, tx_hash, tx_num, error_info) values(#{blockHeight}, #{blockHash}, #{txHash}, #{txNum}, #{errorInfo})")
    Integer insertSyncError(SyncError syncError);
}
