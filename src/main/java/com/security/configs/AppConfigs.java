package com.security.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@Configuration
public class AppConfigs {

    @Bean
    public ModelMapper getModelMapper(){
        return new ModelMapper();
    }

}
