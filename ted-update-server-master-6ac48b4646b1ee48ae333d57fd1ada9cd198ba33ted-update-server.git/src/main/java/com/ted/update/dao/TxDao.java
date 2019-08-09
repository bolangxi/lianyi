package com.ted.update.dao;

import com.ted.update.entity.Tx;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TxDao {
    String TABLE_NAME = "tx";
    String TABLE_COLUMN = "id, hash, from_addr fromAddr, to_addr toAddr, fee, eth_amount, ted_amount, block_state blockState";

    @Insert("insert into " + TABLE_NAME + "(hash, from_addr, to_addr, fee, eth_amount, ted_amount, block_hash, block_height, state, gas_limit, gas_price, tx_id, type, tx_num, asset_short_name, timestamp, comment, nonce, main_coin, contract_addr) values(#{hash}, #{fromAddr}, #{toAddr}, #{fee}, #{ethAmount}, #{tedAmount}, #{blockHash}, #{blockHeight}, #{state}, #{gasLimit}, #{gasPrice}, #{txId}, #{type}, #{txNum}, #{assetShortName}, #{timestamp}, #{comment}, #{nonce}, #{mainCoin}, #{contractAddr})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    Integer insertTx(Tx tx);

    @Select("select id from tx where tx_id=#{txId}")
    Long selectIdByTxId(String txId);

    @Delete("delete from " + TABLE_NAME + " where block_hash=#{blockHash}")
    Integer deleteTxByBlockHash(String blockHash);

    @Select("select " + TABLE_COLUMN + " from " + TABLE_NAME + " where block_hash=#{blockHash}")
    List<Tx> selectTxByBlockHash(String blockHash);

    @Select("select count(id) from " + TABLE_NAME + " where hash=#{txHash} and tx_num=#{txNum}")
    Integer existTx(@Param("txHash") String txHash, @Param("txNum") Integer txNum);

    @Update("update " + TABLE_NAME + " set confirm = #{confirm} where block_hash = #{blockHash}")
    Integer updateConfirmByBlockHash(@Param("blockHash") String blockHash, @Param("confirm") Integer confirm);
}
