package com.biswas.arun.redis.redisapi.common.payload;

import com.biswas.arun.redis.redisapi.common.enums.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
public class ServiceError implements Serializable{
    private String errorMsg;
    private Integer errorCode;
    
    public ServiceError(Integer errorCode, String errorMsg){
        this.errorCode = errorCode;
        String name = ErrorCode.getValue(errorCode).name();
        this.errorMsg = name + " [" + errorMsg + "]";        
        
    }
    
}
