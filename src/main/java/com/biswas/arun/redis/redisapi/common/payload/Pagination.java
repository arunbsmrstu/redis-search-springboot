package com.biswas.arun.redis.redisapi.common.payload;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pagination implements Serializable {
    private Integer page;
    private Integer from;
    private Integer to;
    private Integer items_per_page;
    private Integer total;
    private String err;
    private List<Link> links;
    private String first_page_url;
    private   Integer last_page;
     private String next_page_url;
    private String prev_page_url;
}
