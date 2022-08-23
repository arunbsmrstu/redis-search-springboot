package com.biswas.arun.redis.redisapi.service.redis.imp;

import com.biswas.arun.redis.redisapi.common.enums.ResponseCode;
import com.biswas.arun.redis.redisapi.common.payload.GenericResponse;
import com.biswas.arun.redis.redisapi.common.utils.Defs;
import com.biswas.arun.redis.redisapi.common.utils.Utils;
import com.biswas.arun.redis.redisapi.dto.Customer;
import com.biswas.arun.redis.redisapi.repository.redis.CustomerRedisRepository;
import com.biswas.arun.redis.redisapi.service.redis.CustomerRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerRedisServiceImplementation implements CustomerRedisService {
    private static  final Logger LOGGER = LoggerFactory.getLogger(CustomerRedisService.class);
    @Autowired
    private CustomerRedisRepository customerRedisRepository;
    @Override
    public GenericResponse addCustomer(List<Customer> customerList) {
        if(Utils.isOk(customerList) && !customerList.isEmpty()){
            try {
                customerRedisRepository.deleteAll();
                customerRedisRepository.saveAll(customerList);
                LOGGER.info("Successfully Customer sync at redis.");
                return new GenericResponse(ResponseCode.OK, Defs.OPERATION_SUCCESSFUL);
            }catch (Exception ex){
                LOGGER.error("Error",ex.getMessage());
                return new GenericResponse(ResponseCode.SERVICE_ERROR, ex.getMessage());
            }
        }else {
            return new GenericResponse(ResponseCode.DATA_NOT_FOUND, Defs.OPERATION_FAILED);
        }
    }

    @Override
    public List<Customer> getAllCustomer() {return (List<Customer>)customerRedisRepository.findAll();
    }

    @Override
    public List<Customer> getCustomerById(Long id) {
        List<Customer> customerList=new ArrayList<>();
        Optional<Customer> customerOption=customerRedisRepository.findById(id);
        if (Utils.isOk(customerOption) && customerOption.isPresent()){
            customerList.add(customerOption.get());
        }
        return customerList;

    }
}
