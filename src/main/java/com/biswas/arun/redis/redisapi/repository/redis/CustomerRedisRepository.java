package com.biswas.arun.redis.redisapi.repository.redis;

import com.biswas.arun.redis.redisapi.dto.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRedisRepository extends CrudRepository<Customer, Long> {
}
