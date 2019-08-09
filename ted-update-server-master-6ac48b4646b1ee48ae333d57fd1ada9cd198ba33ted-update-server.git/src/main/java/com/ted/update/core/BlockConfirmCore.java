package com.ted.update.core;

import com.ted.update.entity.Block;
import com.ted.update.enums.ConfirmEnum;
import com.ted.update.service.BlockHandlerService;
import com.ted.update.service.BlockService;
import com.ted.update.util.TedLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlockConfirmCore implements TedLogger {
    @Autowired
    private BlockService blockService;
    @Autowired
    private BlockHandlerService blockHandlerService;
    @Value("${blockchain.block.confirm: 10}")
    private Integer blockConfirm = 10;

    public void blockConfirmHandler() {
        Integer height = blockHandlerService.getBlockNumber();
        List<Block> blockList = blockService.selectBlocksByConfirm(height - 12);
        blockList.forEach(block -> {
            try {
                String currentHash = blockHandlerService.getBlockHash(block.getHeight());
                if(currentHash.equals(block.getHash())){
                    blockService.blockConfirm(block, ConfirmEnum.SUCCESS.getCode());
                } else {
                    blockService.blockConfirm(block, ConfirmEnum.FAIL.getCode());
                }
            }catch (Exception e) {
                logger.error(e.getMessage());
            }
        });
    }
}
