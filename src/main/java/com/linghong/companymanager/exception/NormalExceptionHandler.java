package com.linghong.companymanager.exception;


import com.linghong.companymanager.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class NormalExceptionHandler {

//    @ExceptionHandler({NullPointerException.class})
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public Response nullPointerException(NullPointerException ex){
//        return new Response(false,707 , null,"请重新登录");
//    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response illegalArgumentException(IllegalArgumentException ex){
        return new Response(false,708 , null,"请携带相应的请求参数"+ex.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response noSuchElementException(NoSuchElementException ex){
        return new Response(false,709 , null, "所传参数不存在");
    }
}
