package com.biswas.arun.redis.redisapi.payload;

import com.biswas.arun.redis.redisapi.common.enums.ResponseCode;
import com.biswas.arun.redis.redisapi.common.payload.GenericResponse;
import com.biswas.arun.redis.redisapi.dto.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse extends GenericResponse implements Serializable {
    private Customer customer;

    public CustomerResponse(ResponseCode code, String message) {
        super(code,message);
    }

    public CustomerResponse(ResponseCode code) {
        super(code);
    }

    public CustomerResponse(ResponseCode code, String message, Object[] results) {
        super(code,message);
        if (results != null && results.length >= 1) {
            this.customer = (Customer)results[0];
        }

    }
}