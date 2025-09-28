package com.chefia.infra.bean;

import com.chefia.adapters.user.outputs.JdbcUserRepository;
import com.chefia.core.port.output.UserRepositoryOutputPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
public class UserBeanConfig {
    @Bean
    public UserRepositoryOutputPort registerUserRepositoryOutputPort(JdbcClient jdbcClient) {
        return new JdbcUserRepository(jdbcClient);
    }
}
