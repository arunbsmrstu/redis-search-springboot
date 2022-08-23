package com.biswas.arun.redis.redisapi.common.payload;

import com.biswas.arun.redis.redisapi.common.enums.SortOrderEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Criteria implements Serializable {
    private Integer startIndex;
    private Integer limit;
    private SortOrderEnum order;
    private String sortColumnName;
}
