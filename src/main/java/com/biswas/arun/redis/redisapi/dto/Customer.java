package com.biswas.arun.redis.redisapi.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash("com.biswas.arun.redis.redisapi.dto.Customer")
public class Customer implements Serializable {
    @Id
    private long id;
    private String fullName;
    private String email;
    private String mobile;
    private String address;
    private Date registeredOn;
    private Date updateAt;

    public Customer(com.biswas.arun.redis.redisapi.model.Customer customer) {
        this.id = customer.getId().longValue();
        this.fullName = customer.getFullName();
        this.email = customer.getEmail();
        this.mobile = customer.getMobile();
        this.address = customer.getAddress();
        this.registeredOn = customer.getCreateAt();
        this.updateAt = customer.getUpdateAt();
    }
}
