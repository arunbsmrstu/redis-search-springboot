package com.biswas.arun.redis.redisapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build();
    }



    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Partners REST API",
                "partnerapi custom description of API.",
                "1.0.0",
                "This is a properties of Momagic Bangladesh",
                new Contact("Momagic Bangladesh", "https://momagicbd.com/", "arun.biswas@momagicbd.com"),
                "License of API by Momagic Bangladesh",
                "API license https://momagicbd.com/",
                Collections.emptyList());
    }
}
