package com.ted.resonance.service;

import com.ted.resonance.entity.response.AdventurerPageVO;
import com.ted.resonance.entity.response.ExpectContributionPageVO;

public interface AdventurerSubAwardService {

    /**
     * 冒险家活动分奖
     * @param activityId
     * @return
     */
    void subAward(Long activityId);

}
