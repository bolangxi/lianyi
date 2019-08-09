package com.ted.update.service;

import com.ted.update.entity.Account;
import com.ted.update.entity.Tx;

import java.util.List;

public interface TxService {
    Integer insertTx(Tx tx);

    Long selectIdByTxId(String txId);

    Integer deleteTxByBlockHash(String blockHash);

    List<Tx> selectTxByBlockHash(String blockHash);

    Integer existTx(String txHash, Integer txNum);

    Integer updateConfirmByBlockHash(String blockHash, Integer confirm);
}
