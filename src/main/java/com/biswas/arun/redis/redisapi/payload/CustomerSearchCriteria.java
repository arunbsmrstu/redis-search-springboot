package com.biswas.arun.redis.redisapi.payload;

import com.biswas.arun.redis.redisapi.common.annotation.FromDate;
import com.biswas.arun.redis.redisapi.common.annotation.Like;
import com.biswas.arun.redis.redisapi.common.annotation.ToDate;
import com.biswas.arun.redis.redisapi.common.payload.Criteria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CustomerSearchCriteria extends Criteria implements Serializable {
    private Long id;
    private String fullName;
    private String email;
    private String mobile;
    private String startCreateAt;
    private String endCreateAt;
}
