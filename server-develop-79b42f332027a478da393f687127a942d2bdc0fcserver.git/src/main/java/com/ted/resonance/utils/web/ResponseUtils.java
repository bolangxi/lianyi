package com.ted.resonance.utils.web;

import org.springframework.http.HttpStatus;

public class ResponseUtils {
    //一般成功消息
    public static ResponseEntity makeOkResponse(){
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setStatus(200);
        responseEntity.setMessage("success");
        return responseEntity;
    }

    //带数据的返回消息
    public static <T> ResponseEntity makeOkResponse(T content) {
        ResponseEntity<T> responseEntity = new ResponseEntity();
        responseEntity.setStatus(200);
        responseEntity.setMessage("success");
        responseEntity.setContent(content);
        return responseEntity;
    }

    //返回错误
    public static ResponseEntity makeErrorResponse(int code, String message) {
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setStatus(code);
        responseEntity.setMessage(message);
        return responseEntity;
    }

    public static ResponseEntity makeErrorResponse(String message) {
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setStatus(HttpStatus.PRECONDITION_FAILED.value());
        responseEntity.setMessage(message);
        return responseEntity;
    }
}
