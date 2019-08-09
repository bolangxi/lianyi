package com.ted.update.task;

import com.ted.update.core.BlockTxCore;
import com.ted.update.util.TedLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by qingyun.yu on 2018/11/23.
 */
@Component
@Async
public class BlockTxTask implements TedLogger {
    @Autowired
    private BlockTxCore blockTxCore;

    @Scheduled(cron = "${task.block-tx.scheduled}")
    public void execute() {
        logger.warn("block tx get start");
        blockTxCore.blockTxHandler();
        logger.warn("block tx get complete");
    }
}
