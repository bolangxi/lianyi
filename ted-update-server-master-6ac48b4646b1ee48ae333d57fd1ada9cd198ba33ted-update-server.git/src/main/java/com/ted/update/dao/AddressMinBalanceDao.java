package com.ted.update.dao;

import com.ted.update.entity.AddressMinBalance;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;

@Mapper
public interface AddressMinBalanceDao {
    String TABLE_NAME = "address_min_balance";
    String TABLE_COLUMN = "id, address, day_time dayTime, balance, create_time createTime, update_time updateTime, del";
    @Select("select "+TABLE_COLUMN+" from "+TABLE_NAME+" where address = #{address} and del = 0 order by day_time desc limit 1")
    AddressMinBalance selectAddressMinBalance(String address);

    @Update("update " + TABLE_NAME + " set balance = #{balance} where id = #{id}")
    Integer updateBalanceById(@Param("balance") BigDecimal balance, @Param("id") Long id);

    @Insert("insert into address_min_balance(address, day_time, balance) values(#{address}, #{dayTime}, #{balance})")
    Integer insert(AddressMinBalance addressMinBalance);
}
