package com.example.projectservicev.configuration;

import feign.Logger;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

@Configuration
public class FeignClientConfiguration {
//    @Bean
//    Logger.Level feignLoggerLevel() {
//        return Logger.Level.FULL;
//    }

    @Bean
    @Primary
    @Scope("prototype")
    public Encoder feignFormEncoder() {
    return new SpringFormEncoder();
    }

//    @Bean
//    public Encoder feignFormEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
//        return new SpringFormEncoder(new SpringEncoder(messageConverters));
//    }
}
