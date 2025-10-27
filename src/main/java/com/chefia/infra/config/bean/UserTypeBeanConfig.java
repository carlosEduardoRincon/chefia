package com.chefia.infra.config.bean;

import com.chefia.infra.database.jdbc.repository.JdbcUserTypeRepository;
import com.chefia.core.gateway.UserTypeGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
public class UserTypeBeanConfig {
    @Bean
    public UserTypeGateway registerUserTypeRepositoryOutputPort(JdbcClient jdbcClient) {
        return new JdbcUserTypeRepository(jdbcClient);
    }
}
