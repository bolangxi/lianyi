package com.ted.update.service;



import com.ted.update.entity.Block;

import java.util.List;

public interface BlockService {
    Integer insertBlock(Block block);

    Integer insertBlocks(List<Block> blockList);

    Integer selectMaxHeight();

    List<Block> selectBlocks();

    List<Block> selectBlocksByHeight(Integer height);

    Block selectBlockByHeight(Integer height);

    Integer updateBlockSynced(Long id);

    Integer deleteBlockByBlockHash(String blockHash);

    Integer selectCountBlock();

    Integer updateBlockSyncedByHash(String blockHash);

    Integer updateBlockConfirm(Long id, Integer confirm);

    List<Block> selectBlocksByConfirm(Integer height);

    void blockConfirm(Block block, Integer confirm);
}
