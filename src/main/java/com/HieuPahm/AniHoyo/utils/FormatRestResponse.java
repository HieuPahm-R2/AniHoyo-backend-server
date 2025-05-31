package com.HieuPahm.AniHoyo.utils;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.HieuPahm.AniHoyo.domain.RestResponse;
import com.HieuPahm.AniHoyo.utils.anotation.MessageApi;

import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {
     @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }
    @Override
    @Nullable
    public Object beforeBodyWrite(@Nullable Object arg0, 
    MethodParameter arg1, 
    MediaType arg2, 
    Class arg3,
    ServerHttpRequest arg4, ServerHttpResponse arg5) {
        HttpServletResponse servletResponse = ((ServletServerHttpResponse) arg5).getServletResponse();
        int status = servletResponse.getStatus();
        RestResponse<Object> res = new RestResponse<>();
        if(arg0 instanceof String){
            return arg0;
        }
        //========= error case=========
        if(status >= 400){
            return arg0;
        }else{
            // ====== success case =======
            res.setData(arg0);
            MessageApi message = arg1.getMethodAnnotation(MessageApi.class);
            res.setMessage(message != null ? message.value() : "API HAS BEEN SUCCESSFULLY CALLED");
        }
        return res;
    }

   
    
}
