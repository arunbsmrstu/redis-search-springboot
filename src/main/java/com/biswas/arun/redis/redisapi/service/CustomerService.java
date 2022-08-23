package com.biswas.arun.redis.redisapi.service;

import com.biswas.arun.redis.redisapi.common.payload.GenericResponse;
import com.biswas.arun.redis.redisapi.dto.Customer;
import com.biswas.arun.redis.redisapi.payload.*;
import org.springframework.stereotype.Service;
@Service
public interface CustomerService {
    public CustomerResponse createCustomer(CustomerRequest customerRequest);
    public CustomerResponse updateCustomer(CustomerUpdateRequest customerUpdateRequest);
    public GenericResponse  deleteCustomer(Long id);
    public CustomerSearchResponse searchCustomer(CustomerSearchCriteria customerSearchCriteria);
    public CustomerResponse getCustomerDetails(Long id);
}
