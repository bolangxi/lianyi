package com.ted.resonance.service;

import com.ted.resonance.entity.request.WithdrawApplyRequest;
import com.ted.resonance.entity.request.WithdrawBroadcastRequest;
import com.ted.resonance.entity.response.WithdrawMessageResponse;
import com.ted.resonance.utils.web.ResponseEntity;


public interface WithdrawOrderService {
    ResponseEntity withdrawApply(WithdrawApplyRequest withDrawApplyRequest);

    Boolean withdrawBroadcast(WithdrawBroadcastRequest withdrawBroadcastRequest);

    void updateStatusByRefNo(WithdrawMessageResponse withdrawMessageResponse);

}
