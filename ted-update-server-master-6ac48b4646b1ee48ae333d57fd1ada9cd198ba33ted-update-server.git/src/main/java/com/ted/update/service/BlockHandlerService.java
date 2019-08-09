package com.ted.update.service;


import com.ted.update.entity.Block;
import com.ted.update.entity.Tx;

import java.math.BigDecimal;
import java.util.List;

public interface BlockHandlerService {
    Integer getBlockNumber();

    List<Block> getBlocks(List<Integer> blockHeights);

    List<Tx> getBlockTx(Block block);

    String getBlockHash(Integer blockHeight);

    Integer getMemPoolTxCount();

    BigDecimal getAddressBalance(String address);
}
