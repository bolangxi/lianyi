package com.ted.update.task;

import com.ted.update.core.BlockConfirmCore;
import com.ted.update.util.TedLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Async
public class BlockConfirmTask implements TedLogger {
    @Autowired
    private BlockConfirmCore blockConfirmCore;
    @Scheduled(cron = "${task.block-confirm.scheduled}")
    public void execute() {
        logger.warn("block confirm start");
        blockConfirmCore.blockConfirmHandler();
        logger.warn("block confirm complete");
    }
}
