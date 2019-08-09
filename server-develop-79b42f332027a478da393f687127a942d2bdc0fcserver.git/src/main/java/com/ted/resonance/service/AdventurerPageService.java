package com.ted.resonance.service;

import com.ted.resonance.entity.CaCollarCouponsBean;
import com.ted.resonance.entity.CaDrawChanceBean;
import com.ted.resonance.entity.response.AdventurerPageVO;
import com.ted.resonance.entity.response.ExpectContributionPageVO;
import com.ted.resonance.utils.web.ResponseEntity;

public interface AdventurerPageService {

    /**
     * 活动页面
     * @param address
     * @return
     */
    AdventurerPageVO ActivityPage(String address);

    /**
     * 根据TED 计算预计获得贡献值
     * @param ted
     * @return
     */
    ExpectContributionPageVO getContribution(Integer ted);


}
