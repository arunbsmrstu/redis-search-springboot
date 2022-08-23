package com.biswas.arun.redis.redisapi.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest implements Serializable {
    @NotNull(message = "FullName is Mandatory")
    private String fullName;
    @NotNull(message = "Email is Mandatory")
    private String email;
    @NotNull(message = "Mobile is Mandatory")
    private String mobile;
    private String address;
}
