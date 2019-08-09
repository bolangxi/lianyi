package com.ted.resonance.controller;

import com.ted.resonance.config.DServiceConstant;
import com.ted.resonance.config.DataDictCache;
import com.ted.resonance.entity.CaCollarCouponsBean;
import com.ted.resonance.entity.CaDrawChanceBean;
import com.ted.resonance.entity.CaRecordInfoBean;
import com.ted.resonance.entity.UserAccDetailBean;
import com.ted.resonance.entity.request.BalanceRequest;
import com.ted.resonance.entity.request.PayCouponsRequest;
import com.ted.resonance.entity.response.BalanceResponse;
import com.ted.resonance.service.CaCollarCouponsService;
import com.ted.resonance.service.CaDrawChanceService;
import com.ted.resonance.service.CaRecordInfoService;
import com.ted.resonance.service.UserAccService;
import com.ted.resonance.utils.ParamValidUtil;
import com.ted.resonance.utils.exceptions.CommonException;
import com.ted.resonance.utils.log.LogProxy;
import com.ted.resonance.utils.web.ResponseEntity;
import com.ted.resonance.utils.web.ResponseUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//炼金师处理类
@RestController
@RequestMapping("/alchemist")
public class AlchemistController {
    @Autowired
    private CaRecordInfoService caRecordInfoService;

    @Autowired
    private CaDrawChanceService caDrawChanceService;

    @Autowired
    private CaCollarCouponsService caCollarCouponsService;

    @Autowired
    private UserAccService userAccService;

    @LogProxy("加入炼金师")
    @ApiOperation("加入炼金师")
    @GetMapping(value = "/income", produces = "application/json")
    public ResponseEntity<CaRecordInfoBean> incomeAlchemist(@RequestParam @ApiParam("地址信息") String addr) {
        boolean paramFlag = ParamValidUtil.validParams(addr);
        if (!paramFlag) {
            throw new CommonException(DServiceConstant.PARAMS_ERROR.getCode(), DServiceConstant.PARAMS_ERROR.getMsg());
        }
        return ResponseUtils.makeOkResponse(caRecordInfoService.addCaRecordInfo(addr));
    }

    @LogProxy("已加入炼金师详细信息显示")
    @ApiOperation("已加入炼金师显示")
    @GetMapping(value = "/income/detail", produces = "application/json")
    public ResponseEntity<CaDrawChanceBean> incomeAlchemistDetail(@RequestParam @ApiParam("地址信息") String addr) {
        return ResponseUtils.makeOkResponse(caDrawChanceService.findCaDrawChanceByAddr(addr));
    }

    @LogProxy("用户开奖操作")
    @ApiOperation("开奖")
    @GetMapping(value = "/draw", produces = "application/json")
    public ResponseEntity<CaCollarCouponsBean> draw(@RequestParam @ApiParam("当前的机会表Id") Long id, @RequestParam @ApiParam("地址信息") String addr) {
        boolean paramFlag = ParamValidUtil.validParams(id, addr);
        if (!paramFlag) {
            throw new CommonException(DServiceConstant.PARAMS_ERROR.getCode(), DServiceConstant.PARAMS_ERROR.getMsg());
        }
        return ResponseUtils.makeOkResponse(caDrawChanceService.updateByCouponId(id, addr));
    }


    @LogProxy("领金券列表信息展示")
    @ApiOperation("领金券列表")
    @GetMapping(value = "/couponsList", produces = "application/json")
    public ResponseEntity<CaCollarCouponsBean> couponsList(@RequestParam @ApiParam("地址信息") String addr) {
        boolean paramFlag = ParamValidUtil.validParams(addr);
        if (!paramFlag) {
            throw new CommonException(DServiceConstant.PARAMS_ERROR.getCode(), DServiceConstant.PARAMS_ERROR.getMsg());
        }
        return ResponseUtils.makeOkResponse(caCollarCouponsService.couponsList(addr));
    }

    @LogProxy("领金券支付操作")
    @ApiOperation("领金券支付")
    @PostMapping(value = "/payCoupons", produces = "application/json")
    public ResponseEntity<CaCollarCouponsBean> payCoupons(@RequestBody PayCouponsRequest payCouponsRequest) {
        return ResponseUtils.makeOkResponse(caCollarCouponsService.payCoupons(payCouponsRequest));
    }

    @LogProxy("炼金师活动是否开启校验")
    @ApiOperation("炼金师活动是否开启")
    @GetMapping(value = "/caIsOpen", produces = "application/json")
    public ResponseEntity<CaCollarCouponsBean> caIsOpen() {
        return ResponseUtils.makeOkResponse(DataDictCache.getDataValue("caSwitch"));
    }

    @LogProxy("查询用户的提现列表信息")
    @ApiOperation("查询用户的提现列表信息")
    @GetMapping(value = "/withDrawList", produces = "application/json")
    public ResponseEntity<UserAccDetailBean> withDrawList(@RequestParam @ApiParam("当前登录用户ID") String addr, @RequestParam @ApiParam("当前使用的币种类型") String coinType) {
        boolean paramFlag = ParamValidUtil.validParams(coinType, addr);
        if (!paramFlag) {
            throw new CommonException(DServiceConstant.PARAMS_ERROR.getCode(), DServiceConstant.PARAMS_ERROR.getMsg());
        }
        coinType = coinType.toUpperCase();
        return ResponseUtils.makeOkResponse(userAccService.selectDetailByAddrAndCoinType(addr, coinType));
    }

    @LogProxy("收获(支持单个币种余额查询)")
    @ApiOperation("收获(支持单个币种余额查询)")
    @PostMapping(value = "/selectBalances", produces = "application/json")
    public ResponseEntity<List<BalanceResponse>> selectBalances(@RequestBody BalanceRequest request) {
        boolean paramFlag = ParamValidUtil.validParams(request.getAddr());
        if (!paramFlag) {
            throw new CommonException(DServiceConstant.PARAMS_ERROR.getCode(), DServiceConstant.PARAMS_ERROR.getMsg());
        }
        return ResponseUtils.makeOkResponse(caCollarCouponsService.selectBalances(request));
    }

    @LogProxy("获取提现余额和提现手续费")
    @ApiOperation("获取提现余额和提现手续费")
    @PostMapping(value = "/withDrawBalance", produces = "application/json")
    public ResponseEntity<Map<String, Object>> withDrawBalance(@RequestBody BalanceRequest BalanceRequest) {
        return ResponseUtils.makeOkResponse(caCollarCouponsService.withDrawBalance(BalanceRequest));
    }


}
