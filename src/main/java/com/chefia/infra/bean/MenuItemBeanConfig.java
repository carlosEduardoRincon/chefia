package com.chefia.infra.bean;

import com.chefia.adapters.menuitem.outputs.JdbcMenuItemRepository;
import com.chefia.core.port.output.menuitem.MenuItemRepositoryOutputPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
public class MenuItemBeanConfig {

    @Bean
    public MenuItemRepositoryOutputPort registerMenuItemRepositoryOutputPort(JdbcClient jdbcClient) {
        return new JdbcMenuItemRepository(jdbcClient);
    }
}
