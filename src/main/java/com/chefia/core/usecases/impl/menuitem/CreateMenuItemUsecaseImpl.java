package com.chefia.core.usecases.impl.menuitem;

import com.chefia.core.gateway.MenuItemGateway;
import com.chefia.core.mapper.MenuItemMapper;
import com.chefia.core.usecases.interfaces.menuitem.CreateMenuItemUsecase;
import com.chefia.menuitems.model.CreateMenuItemDTO;
import com.chefia.menuitems.model.MenuItemDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class CreateMenuItemUsecaseImpl implements CreateMenuItemUsecase {

    private final MenuItemGateway menuItemGateway;
    private final MenuItemMapper menuItemMapper;

    @Override
    public MenuItemDTO execute(CreateMenuItemDTO createMenuItemDTO) {
        var menuItemToInsert = this.menuItemMapper.toEntity(createMenuItemDTO);
        this.menuItemGateway.save(menuItemToInsert);

        return this.menuItemMapper.toMenuItemResponseDTO(menuItemToInsert);
    }
}
