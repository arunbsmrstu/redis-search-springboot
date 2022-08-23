package com.biswas.arun.redis.redisapi.service.redis;

import com.biswas.arun.redis.redisapi.common.payload.GenericResponse;
import com.biswas.arun.redis.redisapi.dto.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerRedisService {
    public GenericResponse addCustomer(List<Customer> customerList);
    public List<Customer> getAllCustomer();
    public List<Customer> getCustomerById(Long id);
}
