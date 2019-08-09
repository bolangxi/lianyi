package com.ted.update.dao;

import com.ted.update.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AccountDao {
    String TABLE_NAME = "account";
    String TABLE_COLUMN = "id, address, nickname, head_picture headPicture, nation";

    @Select("select " + TABLE_COLUMN + " from " + TABLE_NAME + " where addr=#{fromAddr} union all " + " select " + TABLE_COLUMN + " from " + TABLE_NAME + " where addr=#{toAddr}")
    List<Account> selectAccountByAddress(@Param("fromAddr") String fromAddr, @Param("toAddr") String toAddr);

}
