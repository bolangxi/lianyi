package com.ted.update.service.impl;

import com.ted.update.dao.CaDrawChanceDao;
import com.ted.update.service.CaDrawChanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CaDrawChanceServiceImpl implements CaDrawChanceService {
    @Autowired
    private CaDrawChanceDao caDrawChanceDao;
    @Override
    public Integer updateChanceTimes() {
        return caDrawChanceDao.updateChanceTimes();
    }
}
