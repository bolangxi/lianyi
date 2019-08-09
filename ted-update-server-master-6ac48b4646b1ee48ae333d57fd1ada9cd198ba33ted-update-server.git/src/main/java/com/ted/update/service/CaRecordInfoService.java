package com.ted.update.service;

import com.ted.update.entity.CaRecordInfo;

import java.util.Date;
import java.util.List;

public interface CaRecordInfoService {
    List<CaRecordInfo> selectCaRecordInfo(Date dayTime);

    Integer updateDayTime(Date dayTime, Long id);
}
