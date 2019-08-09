package com.ted.update.service.impl;

import com.ted.update.dao.AddressMinBalanceDao;
import com.ted.update.dao.CaRecordInfoDao;
import com.ted.update.entity.AddressMinBalance;
import com.ted.update.entity.CaRecordInfo;
import com.ted.update.service.AddressMinBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AddressMinBalanceServiceImpl implements AddressMinBalanceService {
    @Autowired
    private AddressMinBalanceDao addressMinBalanceDao;
    @Autowired
    private CaRecordInfoDao caRecordInfoDao;

    @Override
    public AddressMinBalance selectAddressMinBalance(String address) {
        return addressMinBalanceDao.selectAddressMinBalance(address);
    }

    @Override
    public Integer updateBalanceById(BigDecimal balance, Long id) {
        return addressMinBalanceDao.updateBalanceById(balance, id);
    }

    @Override
    public Integer insert(AddressMinBalance addressMinBalance) {
        return addressMinBalanceDao.insert(addressMinBalance);
    }

    @Override
    @Transactional
    public void saveAddressMinBalance(CaRecordInfo caRecordInfo, AddressMinBalance addressMinBalance) {
        addressMinBalanceDao.insert(addressMinBalance);
        caRecordInfoDao.updateDayTime(caRecordInfo.getDayTime(), caRecordInfo.getId());
    }
}
