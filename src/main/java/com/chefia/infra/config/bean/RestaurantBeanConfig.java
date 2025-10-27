package com.chefia.infra.config.bean;

import com.chefia.infra.database.jdbc.repository.JdbcRestaurantRepository;
import com.chefia.core.gateway.RestaurantGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
public class RestaurantBeanConfig {

    @Bean
    public RestaurantGateway registerRestaurantRepositoryOutputPort(JdbcClient jdbcClient) {
        return new JdbcRestaurantRepository(jdbcClient);
    }
}
