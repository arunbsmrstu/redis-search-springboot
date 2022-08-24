package com.biswas.arun.redis.redisapi.common.payload;

import lombok.*;

import java.io.Serializable;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Link implements Serializable {
    private String url;
    private String label;
    private Integer page;
    private boolean active;
}
