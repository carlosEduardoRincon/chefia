package com.chefia.core.usecases.interfaces.menuitem;

import com.chefia.menuitems.model.CreateMenuItemDTO;
import com.chefia.menuitems.model.MenuItemDTO;

public interface CreateMenuItemUsecase {

    MenuItemDTO execute(CreateMenuItemDTO createMenuItemDTO);
}
