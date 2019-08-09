package com.ted.update.service;

import com.ted.update.entity.AddressMinBalance;
import com.ted.update.entity.CaRecordInfo;

import java.math.BigDecimal;

public interface AddressMinBalanceService {
    AddressMinBalance selectAddressMinBalance(String address);

    Integer updateBalanceById(BigDecimal balance, Long id);

    Integer insert(AddressMinBalance addressMinBalance);

    void saveAddressMinBalance(CaRecordInfo caRecordInfo, AddressMinBalance addressMinBalance);
}
