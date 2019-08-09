package com.ted.resonance.controller;


import com.ted.resonance.entity.request.WithdrawApplyRequest;
import com.ted.resonance.entity.request.WithdrawBroadcastRequest;
import com.ted.resonance.service.WithdrawOrderService;
import com.ted.resonance.utils.log.LogProxy;
import com.ted.resonance.utils.web.ResponseEntity;
import com.ted.resonance.utils.web.ResponseUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author
 */
@RestController
@RequestMapping("/withdraw")
public class WithdrawController {

    @Autowired
    private WithdrawOrderService withdrawOrderService;

    /**
     * 提币申请
     *
     * @param withDrawApplyRequest
     * @return
     */
    @ApiOperation(value = "提币申请")
    @PostMapping("/apply")
    @LogProxy("提币申请")
    public ResponseEntity withdrawApply(@RequestBody WithdrawApplyRequest withDrawApplyRequest) {
        return withdrawOrderService.withdrawApply(withDrawApplyRequest);
    }

    /**
     * 提币广播交易
     *
     * @param withdrawBroadcastRequest
     * @return
     */
    @ApiOperation(value = "提币广播交易")
    @PostMapping("/broadcast")
    @LogProxy("提币广播交易")
    public ResponseEntity withdrawBroadcast(@RequestBody WithdrawBroadcastRequest withdrawBroadcastRequest) {
        withdrawOrderService.withdrawBroadcast(withdrawBroadcastRequest);
        return ResponseUtils.makeOkResponse();
    }
}
