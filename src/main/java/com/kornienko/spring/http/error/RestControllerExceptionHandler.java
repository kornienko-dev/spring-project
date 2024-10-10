package com.kornienko.spring.http.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice(basePackages = "com.kornienko.spring.http.rest")
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

}
