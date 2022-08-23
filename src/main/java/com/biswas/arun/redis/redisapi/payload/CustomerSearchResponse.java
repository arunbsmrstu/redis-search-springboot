package com.biswas.arun.redis.redisapi.payload;

import com.biswas.arun.redis.redisapi.common.enums.ResponseCode;
import com.biswas.arun.redis.redisapi.common.payload.GenericResponse;
import com.biswas.arun.redis.redisapi.dto.Customer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CustomerSearchResponse extends GenericResponse implements Serializable {
    private Integer totalCount;
    private List<Customer> customerList;

    public CustomerSearchResponse(Object[] results) {
        super(ResponseCode.OK);
        if (results != null && results.length >= 2) {
            this.customerList = (ArrayList)results[0];
            this.totalCount = (Integer)results[1];
        }

    }
}