package com.ted.resonance.mapper;


import com.ted.resonance.entity.WithdrawOrderLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface WithdrawOrderLogMapper {

    void insert(WithdrawOrderLog withdrawOrderLog);

    List<WithdrawOrderLog> findAll(@Param("orderId") BigInteger orderId, @Param("event") String event);
}

