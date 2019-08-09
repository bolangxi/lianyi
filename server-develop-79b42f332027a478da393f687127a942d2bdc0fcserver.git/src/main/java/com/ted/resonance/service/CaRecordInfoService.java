package com.ted.resonance.service;

import com.ted.resonance.entity.CaRecordInfoBean;
import com.ted.resonance.utils.web.ResponseEntity;

public interface CaRecordInfoService {

    /**
     * 加入炼金师  通过地址查询用户账户信息，然后增加炼金师记录表信息
     * @param address
     */
    ResponseEntity<CaRecordInfoBean> addCaRecordInfo(String address);
}
