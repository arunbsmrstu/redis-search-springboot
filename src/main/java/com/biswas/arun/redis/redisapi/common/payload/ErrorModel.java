package com.biswas.arun.redis.redisapi.common.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorModel implements Serializable {
    private String fieldName;
    private Object rejectedValue;
    private String messageError;
}
