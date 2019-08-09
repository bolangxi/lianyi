package com.ted.resonance.controller;

import com.ted.resonance.entity.request.BurnRequest;
import com.ted.resonance.service.ExTedBurnService;
import com.ted.resonance.utils.log.LogProxy;
import com.ted.resonance.utils.web.ResponseEntity;
import com.ted.resonance.utils.web.ResponseUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adventurer")
public class AdventurerController {
    @Autowired
    private ExTedBurnService exTedBurnService;

    @LogProxy("燃烧Ted")
    @ApiOperation("燃烧Ted")
    @PostMapping("burn/ted")
    public ResponseEntity burnTed(@RequestBody BurnRequest burnRequest) {
        exTedBurnService.burnTed(burnRequest);
        return ResponseUtils.makeOkResponse();
    }
}
