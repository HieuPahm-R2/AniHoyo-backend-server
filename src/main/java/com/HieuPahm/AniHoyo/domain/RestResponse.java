package com.HieuPahm.AniHoyo.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestResponse<T> {
    private int statusCode;
    private String error;
    // message maybe string or an object
    private Object message;
    private T data;

}
