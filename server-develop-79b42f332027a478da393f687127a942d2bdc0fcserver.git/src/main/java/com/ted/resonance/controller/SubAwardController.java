package com.ted.resonance.controller;

import com.ted.resonance.config.DServiceConstant;
import com.ted.resonance.config.DataDictCache;
import com.ted.resonance.config.DataDictConstant;
import com.ted.resonance.entity.*;
import com.ted.resonance.enums.ActivityStatusEnum;
import com.ted.resonance.enums.OperateType;
import com.ted.resonance.enums.TradeType;
import com.ted.resonance.repository.*;
import com.ted.resonance.service.AdventurerSubAwardService;
import com.ted.resonance.utils.ParamValidUtil;
import com.ted.resonance.utils.exceptions.CommonException;
import com.ted.resonance.utils.log.LogProxy;
import com.ted.resonance.utils.web.ResponseEntity;
import com.ted.resonance.utils.web.ResponseUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;


@RestController
public class SubAwardController {

    @Autowired
    private AdventurerSubAwardService adventurerSubAwardService;

    /**
     * 冒险家活动分奖
     */
    @ApiOperation(value = "冒险家活动分奖请求")
    @GetMapping("/subAward")
    @LogProxy("冒险家活动分奖接口")
    public ResponseEntity subAward(@ApiParam("活动ID")@RequestParam(name = "activityId") Long activityId){
        boolean paramFlag = ParamValidUtil.validParams(activityId);
        if (!paramFlag) {
            throw new CommonException(DServiceConstant.PARAMS_ERROR.getCode(), DServiceConstant.PARAMS_ERROR.getMsg());
        }
        adventurerSubAwardService.subAward(activityId);
        return ResponseUtils.makeOkResponse();
    }

}
