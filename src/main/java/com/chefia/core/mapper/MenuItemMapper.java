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

    public MenuItemDTO toMenuItemResponseDTO(MenuItem menuItemToInsert) {
        return new MenuItemDTO().id(menuItemToInsert.getNrSeqMenuItem())
                .name(menuItemToInsert.getName())
                .description(menuItemToInsert.getDescription())
                .price(menuItemToInsert.getPrice())
                .imagePath(menuItemToInsert.getImagePath())
                .availableOnlyOnSite(menuItemToInsert.getAvailableOnlyOnSite())
                .restaurantId(menuItemToInsert.getRestaurantId());
    }

    public List<MenuItemDTO> toResponseListDTO(List<MenuItem> menuItemList) {
        var menuItemResponse = new ArrayList<MenuItemDTO>();
        for (var menuItem : menuItemList) {
            menuItemResponse.add(this.toMenuItemResponseDTO(menuItem));
        }
        return menuItemResponse;
    }
}
