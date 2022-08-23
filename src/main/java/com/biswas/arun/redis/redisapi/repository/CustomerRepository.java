package com.biswas.arun.redis.redisapi.repository;

import com.biswas.arun.redis.redisapi.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Customer findByEmail(String email);
    Customer findByMobile(String mobile);
}
