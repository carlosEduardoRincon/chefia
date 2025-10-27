package com.chefia.core.usecases.interfaces.menuitem;

import com.chefia.menuitems.model.MenuItemDTO;
import com.chefia.menuitems.model.UpdateMenuItemDTO;

public interface UpdateMenuItemUsecase {
    MenuItemDTO execute(Long menuItemId, UpdateMenuItemDTO body);
}
