package com.chefia.infra.mapper;

import com.chefia.domain.model.MenuItem;
import com.chefia.menuitems.model.CreateMenuItemDTO;
import com.chefia.menuitems.model.MenuItemDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MenuItemMapper {
    public MenuItem toEntity(CreateMenuItemDTO createMenuItemDTO) {
        return new MenuItem();
    }

    public MenuItemDTO toMenuItemResponseDTO(MenuItem menuItemToInsert) {
        return new MenuItemDTO();
    }

    public List<MenuItemDTO> toResponseListDTO(List<MenuItem> content) {
        return new ArrayList<>();
    }
}
