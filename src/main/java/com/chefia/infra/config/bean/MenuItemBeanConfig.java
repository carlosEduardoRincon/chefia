package com.chefia.infra.config.bean;

import com.chefia.infra.database.jdbc.repository.JdbcMenuItemRepository;
import com.chefia.core.gateway.MenuItemGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
public class MenuItemBeanConfig {

    @Bean
    public MenuItemGateway registerMenuItemRepositoryOutputPort(JdbcClient jdbcClient) {
        return new JdbcMenuItemRepository(jdbcClient);
    }
}
