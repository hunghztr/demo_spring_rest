package com.example.demo.util;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.demo.domain.dto.RestResponse;

@ControllerAdvice
public class FormatResponse implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
            ServerHttpResponse response) {
        ServletServerHttpResponse sResponse = (ServletServerHttpResponse) response;
        HttpStatus status = HttpStatus.valueOf(sResponse.getServletResponse().getStatus());

        RestResponse<Object> rest = new RestResponse<>();
        rest.setStatus(status.value());
        if (status.value() >= 400) {
            return body;
        }
        rest.setMess("execute success");
        rest.setData(body);
        return rest;
    }

}
