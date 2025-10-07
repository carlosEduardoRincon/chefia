package com.chefia.core.mapper;

import com.chefia.core.entities.MenuItem;
import com.chefia.menuitems.model.CreateMenuItemDTO;
import com.chefia.menuitems.model.MenuItemDTO;
import com.chefia.restaurants.model.RestaurantDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MenuItemMapper {
    public MenuItem toEntity(CreateMenuItemDTO createMenuItemDTO) {
        return new MenuItem(createMenuItemDTO.getName(),
                createMenuItemDTO.getDescription(),
                createMenuItemDTO.getPrice(),
                createMenuItemDTO.isAvailableOnlyOnSite(),
                createMenuItemDTO.getImagePath(),
                createMenuItemDTO.getRestaurantId()
        );
    }

    public MenuItemDTO toMenuItemResponseDTO(MenuItem menuItem) {
        return new MenuItemDTO().id(menuItem.getNrSeqMenuItem())
                .name(menuItem.getName())
                .description(menuItem.getDescription())
                .price(menuItem.getPrice())
                .imagePath(menuItem.getImagePath())
                .availableOnlyOnSite(menuItem.getAvailableOnlyOnSite())
                .restaurantId(menuItem.getRestaurantId());
    }

    public List<MenuItemDTO> toResponseListDTO(List<MenuItem> menuItemList) {
        return menuItemList.stream()
                .map(this::toMenuItemResponseDTO)
                .toList();
    }
}
