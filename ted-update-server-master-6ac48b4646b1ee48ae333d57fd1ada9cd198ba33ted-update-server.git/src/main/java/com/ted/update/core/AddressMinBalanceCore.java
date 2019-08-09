package com.ted.update.core;

import com.ted.update.entity.AddressMinBalance;
import com.ted.update.entity.CaRecordInfo;
import com.ted.update.service.AddressMinBalanceService;
import com.ted.update.service.BlockHandlerService;
import com.ted.update.service.CaDrawChanceService;
import com.ted.update.service.CaRecordInfoService;
import com.ted.update.util.DateUtil;
import com.ted.update.util.TedLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Component
public class AddressMinBalanceCore implements TedLogger {
    @Autowired
    private AddressMinBalanceService addressMinBalanceService;
    @Autowired
    private CaRecordInfoService caRecordInfoService;
    @Autowired
    private BlockHandlerService blockHandlerService;
    @Autowired
    private CaDrawChanceService caDrawChanceService;

    public void addressMinBalanceHandler() {
        try {
            caDrawChanceService.updateChanceTimes();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        while(true) {
            try{
                Date dayTime = DateUtil.initCurrentDate();
                System.out.println("dayTime = " + dayTime);
                List<CaRecordInfo> caRecordInfoList = caRecordInfoService.selectCaRecordInfo(dayTime);
                if(CollectionUtils.isEmpty(caRecordInfoList)) {
                    break;
                }
                caRecordInfoList.forEach(caRecordInfo -> {
                    BigDecimal balance = blockHandlerService.getAddressBalance(caRecordInfo.getAddr());
                    AddressMinBalance addressMinBalance = new AddressMinBalance();
                    addressMinBalance.setAddress(caRecordInfo.getAddr());
                    addressMinBalance.setBalance(balance);
                    addressMinBalance.setDayTime(dayTime);
                    caRecordInfo.setDayTime(dayTime);
                    addressMinBalanceService.saveAddressMinBalance(caRecordInfo, addressMinBalance);
                });
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage());
            }
        }
    }
}
