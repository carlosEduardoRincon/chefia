package com.chefia.core.usecases.interfaces.menuitem;

import com.chefia.menuitems.model.CreateMenuItemDTO;
import com.chefia.menuitems.model.MenuItemDTO;
import com.chefia.menuitems.model.PaginatedMenuItemDTO;
import com.chefia.menuitems.model.UpdateMenuItemDTO;

public interface MenuItemInputPort {

    MenuItemDTO createMenuItem(CreateMenuItemDTO createMenuItemDTO);

    PaginatedMenuItemDTO findAll(Integer page, Integer perPage);

    MenuItemDTO findById(Long menuItemId);

    MenuItemDTO updateMenuItem(Long menuItemId, UpdateMenuItemDTO body);

    void deleteMenuItem(Long menuItemId);
}
