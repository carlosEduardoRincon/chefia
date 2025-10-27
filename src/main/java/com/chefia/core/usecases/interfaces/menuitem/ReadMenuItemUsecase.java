package com.chefia.core.usecases.interfaces.menuitem;

import com.chefia.menuitems.model.MenuItemDTO;

public interface ReadMenuItemUsecase {

    MenuItemDTO execute(Long menuItemId);
}
