package com.ted.update.task;

import com.ted.update.core.AddressMinBalanceCore;
import com.ted.update.util.TedLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Async
public class AddressMinBalanceTask implements TedLogger {
    @Autowired
    private AddressMinBalanceCore addressMinBalanceCore;

    @Scheduled(cron = "${task.address-min-balance.scheduled}")
    public void execute() {
        logger.warn("address min balance start");
        addressMinBalanceCore.addressMinBalanceHandler();
        logger.warn("address min balance end");
    }
}
