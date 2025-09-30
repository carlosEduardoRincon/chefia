package com.chefia.infra.bean;

import com.chefia.adapters.businesshour.outputs.JdbcBusinessHourRepository;
import com.chefia.core.port.output.businesshour.BusinessHourRepositoryOutputPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
public class BusinessHourBeanConfig {

    @Bean
    public BusinessHourRepositoryOutputPort registerBusinessHourRepositoryOutputPort(JdbcClient jdbcClient) {
        return new JdbcBusinessHourRepository(jdbcClient);
    }
}
