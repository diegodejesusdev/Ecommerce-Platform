package com.ecommerce.config;

import com.ecommerce.service.DataInitializerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(DataInitializerService dataInitializerService) {
        return args -> dataInitializerService.initIfEmpty();
    }
}
