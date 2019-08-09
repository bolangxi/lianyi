package com.ted.update.task;

import com.ted.update.core.BlockInfoCore;
import com.ted.update.util.TedLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by qingyun.yu on 2018/11/22.
 */
@Component
@Async
public class BlockInfoTask implements TedLogger {

    @Autowired
    private BlockInfoCore blockInfoCore;
    @Scheduled(cron = "${task.block-info.scheduled}")
    public void execute() {
        logger.warn("block hash get start");
        blockInfoCore.blockInfoHandler();
        logger.warn("block hash get complete");
    }
}
