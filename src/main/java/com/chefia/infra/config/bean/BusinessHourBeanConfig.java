package com.chefia.infra.config.bean;

import com.chefia.infra.database.jdbc.repository.JdbcBusinessHourRepository;
import com.chefia.core.gateway.BusinessHourGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
public class BusinessHourBeanConfig {

    @Bean
    public BusinessHourGateway registerBusinessHourRepositoryOutputPort(JdbcClient jdbcClient) {
        return new JdbcBusinessHourRepository(jdbcClient);
    }
}
