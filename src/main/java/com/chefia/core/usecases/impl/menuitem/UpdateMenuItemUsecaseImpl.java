package com.chefia.core.usecases.impl.menuitem;

import com.chefia.core.exceptions.MenuItemNotFoundException;
import com.chefia.core.gateway.MenuItemGateway;
import com.chefia.core.mapper.MenuItemMapper;
import com.chefia.core.usecases.interfaces.menuitem.UpdateMenuItemUsecase;
import com.chefia.menuitems.model.MenuItemDTO;
import com.chefia.menuitems.model.UpdateMenuItemDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UpdateMenuItemUsecaseImpl implements UpdateMenuItemUsecase {

    private final MenuItemGateway menuItemGateway;
    private final MenuItemMapper menuItemMapper;

    @Override
    public MenuItemDTO execute(Long menuItemId, UpdateMenuItemDTO updateMenuItemDTO) {
        var menuItemEntity = this.menuItemGateway
                .findByMenuItemId(menuItemId)
                .orElseThrow(() -> new MenuItemNotFoundException("Menu Item not found with id: " + menuItemId));

        menuItemEntity.setName(updateMenuItemDTO.getName());
        menuItemEntity.setDescription(updateMenuItemDTO.getDescription());
        menuItemEntity.setPrice(updateMenuItemDTO.getPrice());
        menuItemEntity.setAvailableOnlyOnSite(updateMenuItemDTO.isAvailableOnlyOnSite());
        menuItemEntity.setImagePath(updateMenuItemDTO.getImagePath());

        this.menuItemGateway.update(menuItemId, menuItemEntity);
        return this.menuItemMapper.toMenuItemResponseDTO(menuItemEntity);
    }
}
