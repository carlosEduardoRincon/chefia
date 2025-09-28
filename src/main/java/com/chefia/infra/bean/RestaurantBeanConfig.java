package com.chefia.infra.bean;

import com.chefia.adapters.restaurant.outputs.JdbcRestaurantRepository;
import com.chefia.core.port.output.RestaurantRepositoryOutputPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
public class RestaurantBeanConfig {

    @Bean
    public RestaurantRepositoryOutputPort registerRestaurantRepositoryOutputPort(JdbcClient jdbcClient) {
        return new JdbcRestaurantRepository(jdbcClient);
    }
}
