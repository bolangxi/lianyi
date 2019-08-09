package com.ted.update.service.impl;

import com.ted.update.dao.CaRecordInfoDao;
import com.ted.update.entity.CaRecordInfo;
import com.ted.update.service.CaRecordInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CaRecordInfoServiceImpl implements CaRecordInfoService {
    @Autowired
    private CaRecordInfoDao caRecordInfoDao;
    @Override
    public List<CaRecordInfo> selectCaRecordInfo(Date dayTime) {
        return caRecordInfoDao.selectCaRecordInfo(dayTime);
    }

    @Override
    public Integer updateDayTime(Date dayTime, Long id) {
        return caRecordInfoDao.updateDayTime(dayTime, id);
    }
}
