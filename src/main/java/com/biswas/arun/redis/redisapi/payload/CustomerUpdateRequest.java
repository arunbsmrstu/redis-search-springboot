package com.biswas.arun.redis.redisapi.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUpdateRequest {
    @NotNull(message = "Id is Mandatory")
    private Long id;
    @NotNull(message = "FullName is Mandatory")
    private String fullName;
    @NotNull(message = "Email is Mandatory")
    private String email;
    @NotNull(message = "Mobile is Mandatory")
    private String mobile;
    private String address;
}
