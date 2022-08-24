package com.biswas.arun.redis.redisapi.common.payload;

import lombok.*;

import java.io.Serializable;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payload implements Serializable {
    private Pagination pagination;
}
