package com.chefia.core.usecases.impl.menuitem;

import com.chefia.core.exceptions.MenuItemNotFoundException;
import com.chefia.core.gateway.MenuItemGateway;
import com.chefia.core.mapper.MenuItemMapper;
import com.chefia.core.usecases.interfaces.menuitem.ReadMenuItemUsecase;
import com.chefia.menuitems.model.MenuItemDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class ReadMenuItemUsecaseImpl implements ReadMenuItemUsecase {

    private final MenuItemGateway menuItemGateway;
    private final MenuItemMapper menuItemMapper;

    @Override
    public MenuItemDTO execute(Long menuItemId) {
        var menuItem = Optional.ofNullable(this.menuItemGateway
                .findByMenuItemId(menuItemId)
                .orElseThrow(() -> new MenuItemNotFoundException("Menu Item not found with id: " + menuItemId)));
        assert menuItem.isPresent();
        return this.menuItemMapper.toMenuItemResponseDTO(menuItem.get());
    }
}
