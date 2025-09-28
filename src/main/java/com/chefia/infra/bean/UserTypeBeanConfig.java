package com.chefia.infra.bean;

import com.chefia.adapters.usertype.outputs.JdbcUserTypeRepository;
import com.chefia.core.port.output.usertype.UserTypeRepositoryOutputPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
public class UserTypeBeanConfig {
    @Bean
    public UserTypeRepositoryOutputPort registerUserTypeRepositoryOutputPort(JdbcClient jdbcClient) {
        return new JdbcUserTypeRepository(jdbcClient);
    }
}
