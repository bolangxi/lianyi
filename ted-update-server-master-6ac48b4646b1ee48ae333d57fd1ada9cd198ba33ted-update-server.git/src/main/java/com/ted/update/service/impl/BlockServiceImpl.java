package com.ted.update.service.impl;

import com.ted.update.dao.BlockDao;
import com.ted.update.dao.TxDao;
import com.ted.update.entity.Block;
import com.ted.update.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BlockServiceImpl implements BlockService {
    @Autowired
    private BlockDao blockDao;
    @Autowired
    private TxDao txDao;
    @Override
    public Integer insertBlock(Block block) {
        return blockDao.insertBlock(block);
    }

    @Override
    public Integer insertBlocks(List<Block> blockList) {
        return blockDao.insertBlocks(blockList);
    }

    @Override
    public Integer selectMaxHeight() {
        return blockDao.selectMaxHeight();
    }

    @Override
    public List<Block> selectBlocks() {
        return blockDao.selectBlocks();
    }

    @Override
    public List<Block> selectBlocksByHeight(Integer height) {
        return blockDao.selectBlocksByHeight(height);
    }

    @Override
    public Block selectBlockByHeight(Integer height) {
        return blockDao.selectBlockByHeight(height);
    }

    @Override
    public Integer updateBlockSynced(Long id) {
        return blockDao.updateBlockSynced(id);
    }

    @Override
    public Integer deleteBlockByBlockHash(String blockHash) {
        return blockDao.deleteBlockByBlockHash(blockHash);
    }

    @Override
    public Integer selectCountBlock() {
        return blockDao.selectCountBlock();
    }

    @Override
    public Integer updateBlockSyncedByHash(String blockHash) {
        return blockDao.updateBlockSyncedByHash(blockHash);
    }

    @Override
    public Integer updateBlockConfirm(Long id, Integer confirm) {
        return blockDao.updateBlockConfirm(id, confirm);
    }

    @Override
    public List<Block> selectBlocksByConfirm(Integer height) {
        return blockDao.selectBlocksByConfirm(height);
    }

    @Override
    @Transactional
    public void blockConfirm(Block block, Integer confirm) {
        blockDao.updateBlockConfirm(block.getId(), confirm);
        txDao.updateConfirmByBlockHash(block.getHash(), confirm);
    }
}
