package com.biswas.arun.redis.redisapi.service.imp;

import com.biswas.arun.redis.redisapi.common.enums.ResponseCode;
import com.biswas.arun.redis.redisapi.common.payload.GenericResponse;
import com.biswas.arun.redis.redisapi.common.utils.Defs;
import com.biswas.arun.redis.redisapi.common.utils.Utils;
import com.biswas.arun.redis.redisapi.dto.Customer;
import com.biswas.arun.redis.redisapi.payload.*;
import com.biswas.arun.redis.redisapi.repository.CustomerRepository;
import com.biswas.arun.redis.redisapi.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Optional;

@Service
public class CustomerServiceImplementation implements CustomerService {
    public static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImplementation.class);
    @Autowired
    CustomerRepository customerRepository;

    @Override
    public CustomerResponse createCustomer(CustomerRequest customerRequest) {
        try {
            Object[] results = new Object[1];
            com.biswas.arun.redis.redisapi.model.Customer customerEo = customerRepository.findByEmail(customerRequest.getEmail());
            if (Utils.isOk(customerEo)) {
                return new CustomerResponse(ResponseCode.DUPLICATE_ENTRY, "email already exists");
            }
            customerEo = customerRepository.findByMobile(customerRequest.getMobile());
            if (Utils.isOk(customerEo)) {
                return new CustomerResponse(ResponseCode.DUPLICATE_ENTRY, "mobile already exists");
            }
            customerEo = new com.biswas.arun.redis.redisapi.model.Customer();
            Utils.copyBean(customerRequest, customerEo);
            customerEo.setCreateAt(new Date());
            customerEo.setUpdateAt(new Date());
            com.biswas.arun.redis.redisapi.model.Customer customerSave = customerRepository.save(customerEo);
            results[0] = new Customer(customerSave);
            return new CustomerResponse(ResponseCode.OK, Defs.OPERATION_SUCCESSFUL, results);
        } catch (Exception ex) {
            LOGGER.error("ERROR ()->", ex.getMessage());
            return new CustomerResponse(ResponseCode.SERVICE_ERROR, Defs.OPERATION_FAILED);
        }
    }

    @Override
    public CustomerResponse updateCustomer(CustomerUpdateRequest customerUpdateRequest) {
        try {
            com.biswas.arun.redis.redisapi.model.Customer customerEO = null;
            Object[] results = new Object[1];
            Optional<com.biswas.arun.redis.redisapi.model.Customer> customerEoOptional = customerRepository.findById(customerUpdateRequest.getId());
            if (Utils.isOk(customerEoOptional) && !customerEoOptional.isPresent()) {
                return new CustomerResponse(ResponseCode.DATA_NOT_FOUND, "Invalid Customer id");
            }
            customerEO = customerEoOptional.get();

            com.biswas.arun.redis.redisapi.model.Customer customerEODup = customerRepository.findByEmail(customerUpdateRequest.getEmail());
            if (Utils.isOk(customerEODup) && customerUpdateRequest.getId() != customerEODup.getId() && customerUpdateRequest.getEmail().trim().equalsIgnoreCase(customerEODup.getEmail())) {
                return new CustomerResponse(ResponseCode.DUPLICATE_ENTRY, "email already exists");
            }

            customerEODup = customerRepository.findByMobile(customerUpdateRequest.getMobile());
            if (Utils.isOk(customerEODup) && customerUpdateRequest.getId() != customerEODup.getId() && customerUpdateRequest.getMobile().trim().equalsIgnoreCase(customerEODup.getMobile())) {
                return new CustomerResponse(ResponseCode.DUPLICATE_ENTRY, "Mobile already exists");
            }

            Date createAt = customerEO.getCreateAt();
            Date updateAt = new Date();
            BeanUtils.copyProperties(customerUpdateRequest, customerEO);
            customerEO.setCreateAt(createAt);
            customerEO.setUpdateAt(updateAt);
            com.biswas.arun.redis.redisapi.model.Customer customerSave = customerRepository.save(customerEO);
            results[0] = new Customer(customerSave);
            return new CustomerResponse(ResponseCode.OK, Defs.OPERATION_SUCCESSFUL, results);
        }catch (Exception ex){
            LOGGER.error("ERROR ()->", ex.getMessage());
            return new CustomerResponse(ResponseCode.SERVICE_ERROR, Defs.OPERATION_FAILED);
        }
    }

    @Override
    public GenericResponse deleteCustomer(Long id) {
        try {
            Optional<com.biswas.arun.redis.redisapi.model.Customer> customerEoOptional = customerRepository.findById(id);
            if (Utils.isOk(customerEoOptional) && !customerEoOptional.isPresent()) {
                return new CustomerResponse(ResponseCode.DATA_NOT_FOUND, "Invalid Customer id");
            }
            com.biswas.arun.redis.redisapi.model.Customer customerEO = customerEoOptional.get();
            customerRepository.delete(customerEO);
            return new CustomerResponse(ResponseCode.OK, Defs.OPERATION_SUCCESSFUL);
        }catch (Exception ex){
            LOGGER.error("ERROR ()->", ex.getMessage());
            return new CustomerResponse(ResponseCode.SERVICE_ERROR, Defs.OPERATION_FAILED);
        }
    }

    @Override
    public CustomerSearchResponse searchCustomer(CustomerSearchCriteria customerSearchCriteria) {
        return null;
    }

    @Override
    public CustomerResponse getCustomerDetails(Long id) {
        try {
            Object[] results = new Object[1];
            Optional<com.biswas.arun.redis.redisapi.model.Customer> customerEoOptional = customerRepository.findById(id);
            if (Utils.isOk(customerEoOptional) && !customerEoOptional.isPresent()) {
                return new CustomerResponse(ResponseCode.DATA_NOT_FOUND, "Invalid Customer id");
            }
            com.biswas.arun.redis.redisapi.model.Customer customerEO = customerEoOptional.get();
            results[0]=new Customer(customerEO);
            return new CustomerResponse(ResponseCode.OK, Defs.OPERATION_SUCCESSFUL,results);
        }catch (Exception ex){
            LOGGER.error("ERROR ()->", ex.getMessage());
            return new CustomerResponse(ResponseCode.SERVICE_ERROR, Defs.OPERATION_FAILED);
        }
    }
}
