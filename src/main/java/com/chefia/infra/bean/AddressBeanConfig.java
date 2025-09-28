package com.chefia.infra.bean;

import com.chefia.adapters.address.outputs.JdbcAddressRepository;
import com.chefia.core.port.output.AddressRepositoryOutputPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
public class AddressBeanConfig {

    @Bean
    public AddressRepositoryOutputPort registerAddressRepositoryOutputPort(JdbcClient jdbcClient) {
        return new JdbcAddressRepository(jdbcClient);
    }
}
