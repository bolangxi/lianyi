package com.ted.resonance.controller;

import com.ted.resonance.config.Translator;
import com.ted.resonance.exception.TedException;
import com.ted.resonance.utils.exceptions.CommonException;
import com.ted.resonance.utils.exceptions.SendTransactionException;
import com.ted.resonance.utils.web.ResponseEntity;
import com.ted.resonance.utils.web.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.net.ConnectException;

@ControllerAdvice
@RestController
public class ExceptionController {
    private static Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(SendTransactionException.class)
    public ResponseEntity ex(SendTransactionException ex) {
        return ResponseUtils.makeErrorResponse(999, ex.getMsg());
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity conEx(ConnectException ex) {
        return ResponseUtils.makeErrorResponse(999, Translator.toLocale("connectBlockFailed"));
    }

    @ExceptionHandler(CommonException.class)
    public ResponseEntity comEx(CommonException ex) {
        return ResponseUtils.makeErrorResponse(999, Translator.toLocale(ex.getMsg()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity comEx(Exception ex) {
        LOGGER.error("【系统异常】,异常日志:{}", ex);
        return ResponseUtils.makeErrorResponse(500, Translator.toLocale("systemError"));
    }

    @ExceptionHandler(TedException.class)
    public ResponseEntity comEx(TedException ex) {
        return ResponseUtils.makeErrorResponse(ex.getdServiceConstant().getCode(), Translator.toLocale(ex.getdServiceConstant().getMsg()));
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity allEx(Exception ex) {
//        return ResponseUtils.makeErrorResponse(500, Translator.toLocale("serviceFailed"));
//    }

//    @ExceptionHandler(MessageDecodingException.class)
//    public ResponseEntity conEx(MessageDecodingException ex) {
//        return ResponseUtils.makeErrorResponse(999, "Address " + ex.getMessage());
//    }
}
