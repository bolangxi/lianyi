package com.ted.resonance.mapper;

import com.ted.resonance.entity.TokenBalance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

@Mapper
public interface TokenBalanceBeanMapper {

    @Select("select balance from eth_query.token_balance where addr=#{addr} ")
    BigDecimal findAllByAddr(@Param("addr") String addr);

    @Select("select concat(balance,'') as balance  from eth_query.token_balance where addr=#{addr} and contract_addr=#{contractAddr}")
    BigDecimal findAllByAddrAndContractAddr(@Param("addr") String addr, @Param("contractAddr") String contractAddr);
}
