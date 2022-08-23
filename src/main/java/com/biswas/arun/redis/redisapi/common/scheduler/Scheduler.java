package com.biswas.arun.redis.redisapi.common.scheduler;

import com.biswas.arun.redis.redisapi.common.utils.Utils;
import com.biswas.arun.redis.redisapi.model.Customer;
import com.biswas.arun.redis.redisapi.repository.CustomerRepository;
import com.biswas.arun.redis.redisapi.repository.redis.CustomerRedisRepository;
import com.biswas.arun.redis.redisapi.service.CustomerService;
import com.biswas.arun.redis.redisapi.service.redis.CustomerRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableScheduling
public class Scheduler {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerRedisService customerRedisService;
    @Autowired
    private CustomerRedisRepository customerRedisRepository;
    @Scheduled(fixedRate = 10000)
    public void scheduleFixedRateTask() {
        List<com.biswas.arun.redis.redisapi.dto.Customer> customerList= new ArrayList<>();
        List<Customer> customerEoList=customerRepository.findAll();
        if(Utils.isOk(customerEoList) && !customerEoList.isEmpty()){
            customerEoList.forEach(customerEo -> {
                customerList.add(new com.biswas.arun.redis.redisapi.dto.Customer(customerEo));
            });
            customerRedisService.addCustomer(customerList);
            Iterable<com.biswas.arun.redis.redisapi.dto.Customer>customerIterable=customerRedisRepository.findAll();
            customerIterable.forEach(customer -> {
                System.out.println(customer.getId());
            });
        }
    }
}
