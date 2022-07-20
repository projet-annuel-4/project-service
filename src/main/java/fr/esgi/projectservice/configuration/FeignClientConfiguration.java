package fr.esgi.projectservice.configuration;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
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
