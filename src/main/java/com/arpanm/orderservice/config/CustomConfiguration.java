package com.arpanm.orderservice.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ConfigurationProperties(prefix = "app")
public class CustomConfiguration {

    @NotNull
    private String catalogServiceUrl;
}
