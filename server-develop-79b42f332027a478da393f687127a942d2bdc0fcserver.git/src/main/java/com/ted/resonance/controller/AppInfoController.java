package com.ted.resonance.controller;

import com.ted.resonance.entity.AppInfo;
import com.ted.resonance.repository.AppInfoRepo;
import com.ted.resonance.utils.web.ResponseEntity;
import com.ted.resonance.utils.web.ResponseUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppInfoController {
    @Autowired
    private AppInfoRepo appInfoRepo;

    @GetMapping("/appversion")
    public ResponseEntity<AppInfo> appVersion(@ApiParam("0 android 1 ios") @RequestParam Integer sys) {
        AppInfo appInfo =appInfoRepo.findTopBySysOrderByCreatedAtDesc(sys);
        return ResponseUtils.makeOkResponse(appInfo);
    }
}
