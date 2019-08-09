package com.ted.update.dao;

import com.ted.update.entity.Block;
import com.ted.update.util.ConstantUtil;
import com.ted.update.util.TedLogger;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

@Mapper
public interface BlockDao extends TedLogger {
    String TABLE_NAME = "block";
    String BLOCK_COLUMNS = "id, hash, height, state, previous, tx_count txCount, size, timestamp, synced, create_time, update_time, confirm";

    @Insert("insert into " + TABLE_NAME + "(hash, height, previous, tx_count, size, timestamp) values(#{hash}, #{height}, #{previous},#{txCount},#{size}, #{timestamp})")
    Integer insertBlock(Block block);

    @InsertProvider(type = SqlProvider.class, method = "insertBlocksSql")
    Integer insertBlocks(@Param("blockList") List<Block> blockList);

    @Select("select max(height) from " + TABLE_NAME)
    Integer selectMaxHeight();

    @Select("select " + BLOCK_COLUMNS + " from " + TABLE_NAME + " where synced = " + ConstantUtil.TABLE_BLOCK_UNSYNCED + " order by height asc limit 1000")
    List<Block> selectBlocks();

    @Select("select " + BLOCK_COLUMNS + " from "+TABLE_NAME+" where height <= #{height} and confirm = 0 and synced = 1")
    List<Block> selectBlocksByConfirm(Integer height);

    @Select("select " + BLOCK_COLUMNS + " from " + TABLE_NAME + " where synced = " + ConstantUtil.TABLE_BLOCK_SYNCED + " and state=" + ConstantUtil.TABLE_BLOCK_IS_NOT_ISOLATED + " and height <= #{height}")
    List<Block> selectBlocksByHeight(Integer height);

    @Select("select " + BLOCK_COLUMNS + " from " + TABLE_NAME + " where height = #{height}")
    Block selectBlockByHeight(Integer height);

    @Update("update " + TABLE_NAME + " set synced = " + ConstantUtil.TABLE_BLOCK_SYNCED + " where id = #{id} and synced = " + ConstantUtil.TABLE_BLOCK_UNSYNCED)
    Integer updateBlockSynced(Long id);

    @Update("update " + TABLE_NAME + " set confirm = #{confirm} where id = #{id}")
    Integer updateBlockConfirm(@Param("id") Long id, @Param("confirm") Integer confirm);

    @Delete("delete from " + TABLE_NAME + " where hash = #{blockHash}")
    Integer deleteBlockByBlockHash(String blockHash);

    @Select("select count(id) from " + TABLE_NAME)
    Integer selectCountBlock();

    @Update("update " + TABLE_NAME + " set synced = " + ConstantUtil.TABLE_BLOCK_SYNCED + " where hash = #{blockHash} and synced = " + ConstantUtil.TABLE_BLOCK_UNSYNCED)
    Integer updateBlockSyncedByHash(String blockHash);

    class SqlProvider {
        public String insertBlocksSql(@Param("blockList") List<Block> blockList) {

            StringBuilder sql = new StringBuilder(new SQL() {
                {
                    INSERT_INTO(TABLE_NAME);
                    INTO_COLUMNS("hash", "height", "previous", "tx_count", "size", "timestamp");
                }
            }.toString());
            sql.append("values")
                    .append(blockList.stream()
                            .map(block -> "('" + block.getHash() + "', "
                                    + block.getHeight() + ",'" + block.getPrevious()
                                    + "'," + block.getTxCount() + "," + block.getSize()
                                    + "," + block.getTimestamp() + ")")
                            .reduce((temp, item) -> temp + "," + item).get());
            logger.debug(sql.toString());
            return sql.toString();
        }
    }
}
