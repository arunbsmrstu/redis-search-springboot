package com.biswas.arun.redis.redisapi.common.payload;

import com.biswas.arun.redis.redisapi.common.enums.ResponseCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@Accessors(chain = true)
public class GenericResponse implements Serializable {
    private Integer code;
    private String message;

    public GenericResponse(ResponseCode code) {
        this.code = code.getCode();
    }

    public GenericResponse() {
        this.code =ResponseCode.OK.getCode();
    }

    public GenericResponse(ResponseCode code, String message) {
        this.code = code.getCode();
        this.message = message;
    }
}
