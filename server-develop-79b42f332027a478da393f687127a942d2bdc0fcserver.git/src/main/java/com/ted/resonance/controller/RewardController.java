package com.ted.resonance.controller;

import com.ted.resonance.repository.TransactionRepo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/v1/ted/")
public class RewardController {
    @Autowired
    private TransactionRepo transactionRepo;

    @ApiOperation("开奖接口")
    @GetMapping("openRewardEntry")
    public void openRewardEntry(@RequestParam Integer phase) {
        transactionRepo.updateRewardStatus(phase, 1);
    }

    @ApiOperation("设置第一期结束时间， 格式 yyyy-MM-dd_HH:mm:ss")
    @GetMapping("setPeriodEndTime")
    public void setPeriodEndTime(@RequestParam String date) throws Exception{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        Date endTime = simpleDateFormat.parse(date);
        transactionRepo.setPeriodEndTime(1, endTime);
    }
}
