package com.chefia.infra.config.bean;

import com.chefia.infra.database.jdbc.repository.JdbcUserRepository;
import com.chefia.core.gateway.UserGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
public class UserBeanConfig {
    @Bean
    public UserGateway registerUserRepositoryOutputPort(JdbcClient jdbcClient) {
        return new JdbcUserRepository(jdbcClient);
    }
}
