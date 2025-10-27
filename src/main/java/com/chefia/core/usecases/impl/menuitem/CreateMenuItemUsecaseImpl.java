package com.chefia.core.usecases.impl.menuitem;

import com.chefia.core.entities.MenuItem;
import com.chefia.core.gateway.MenuItemGateway;
import com.chefia.core.gateway.MenuItemValidatorGateway;
import com.chefia.core.mapper.MenuItemMapper;
import com.chefia.core.usecases.interfaces.menuitem.CreateMenuItemUsecase;
import com.chefia.menuitems.model.CreateMenuItemDTO;
import com.chefia.menuitems.model.MenuItemDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class CreateMenuItemUsecaseImpl implements CreateMenuItemUsecase {

    private final MenuItemGateway menuItemGateway;
    private final List<MenuItemValidatorGateway> menuItemValidatorGatewayList;
    private final MenuItemMapper menuItemMapper;

    @Override
    public MenuItemDTO execute(CreateMenuItemDTO createMenuItemDTO) {
        var menuItemToInsert = this.menuItemMapper.toEntity(createMenuItemDTO);

        validateMenuItem(menuItemToInsert);

        var menuItemId = this.menuItemGateway.save(menuItemToInsert);
        menuItemToInsert.setNrSeqMenuItem(menuItemId);

        return this.menuItemMapper.toMenuItemResponseDTO(menuItemToInsert);
    }

    private void validateMenuItem(MenuItem menuItem) {
        for (MenuItemValidatorGateway menuItemValidatorGateway : menuItemValidatorGatewayList) {
            menuItemValidatorGateway.validate(menuItem);
        }
    }
}
