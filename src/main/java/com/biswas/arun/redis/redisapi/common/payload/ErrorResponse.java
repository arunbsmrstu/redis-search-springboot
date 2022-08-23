package com.biswas.arun.redis.redisapi.common.payload;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse implements Serializable {
    private String message;
    private int status;
    private List<ErrorModel> errorMessages;
}
