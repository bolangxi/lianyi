package com.ted.resonance.controller;

import com.ted.resonance.config.DServiceConstant;
import com.ted.resonance.entity.response.AdventurerPageVO;
import com.ted.resonance.entity.response.ExpectContributionPageVO;
import com.ted.resonance.service.AdventurerPageService;
import com.ted.resonance.utils.ParamValidUtil;
import com.ted.resonance.utils.exceptions.CommonException;
import com.ted.resonance.utils.log.LogProxy;
import com.ted.resonance.utils.web.ResponseEntity;
import com.ted.resonance.utils.web.ResponseUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdventurerPageController {

    @Autowired
    private AdventurerPageService adventurerPageService;

    /**
     * 冒险家活动页面
     * @param address
     * @return
     */
    @ApiOperation(value = "冒险家活动页面")
    @GetMapping("/adventurerPage")
    @LogProxy("冒险家活动页面接口")
    public ResponseEntity<AdventurerPageVO> getPage(@ApiParam("用户地址") @RequestParam(required = true, name = "address") String address) {
        boolean paramFlag = ParamValidUtil.validParams(address);
        if (!paramFlag) {
            throw new CommonException(DServiceConstant.PARAMS_ERROR.getCode(), DServiceConstant.PARAMS_ERROR.getMsg());
        }
        return ResponseUtils.makeOkResponse(adventurerPageService.ActivityPage(address));
    }

    /**
     * @param ted
     * @return
     */
    @ApiOperation(value = "冒险家活动页面预计可获得贡献值接口")
    @GetMapping("/getContribution")
    @LogProxy("冒险家活动页面预计可获得贡献值接口")
    public ResponseEntity<ExpectContributionPageVO> getContribution(@ApiParam("输入TED") @RequestParam(required = true, name = "ted")Integer ted) {
        boolean paramFlag = ParamValidUtil.validParams(ted);
        if (!paramFlag) {
            throw new CommonException(DServiceConstant.PARAMS_ERROR.getCode(), DServiceConstant.PARAMS_ERROR.getMsg());
        }
        return ResponseUtils.makeOkResponse(adventurerPageService.getContribution(ted));
    }
}
