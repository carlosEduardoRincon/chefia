package com.chefia.infra.config.bean;

import com.chefia.infra.database.jdbc.repository.JdbcAddressRepository;
import com.chefia.core.gateway.AddressGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
public class AddressBeanConfig {

    @Bean
    public AddressGateway registerAddressRepositoryOutputPort(JdbcClient jdbcClient) {
        return new JdbcAddressRepository(jdbcClient);
    }
}
