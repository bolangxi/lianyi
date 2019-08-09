package com.ted.update.service.impl;

import com.ted.update.dao.TxDao;
import com.ted.update.entity.Tx;
import com.ted.update.service.TxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TxServiceImpl implements TxService {
    @Autowired
    private TxDao txDao;
    @Override
    public Integer insertTx(Tx tx) {
        return txDao.insertTx(tx);
    }

    @Override
    public Long selectIdByTxId(String txId) {
        return txDao.selectIdByTxId(txId);
    }

    @Override
    public Integer deleteTxByBlockHash(String blockHash) {
        return txDao.deleteTxByBlockHash(blockHash);
    }

    @Override
    public List<Tx> selectTxByBlockHash(String blockHash) {
        return txDao.selectTxByBlockHash(blockHash);
    }

    @Override
    public Integer existTx(String txHash, Integer txNum) {
        return txDao.existTx(txHash, txNum);
    }

    @Override
    public Integer updateConfirmByBlockHash(String blockHash, Integer confirm) {
        return txDao.updateConfirmByBlockHash(blockHash, confirm);
    }
}
