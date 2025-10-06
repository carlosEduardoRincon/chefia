package com.chefia.core.controllers;

import com.chefia.core.usecases.interfaces.menuitem.*;
import com.chefia.menuitems.model.CreateMenuItemDTO;
import com.chefia.menuitems.model.MenuItemDTO;
import com.chefia.menuitems.model.PaginatedMenuItemDTO;
import com.chefia.menuitems.model.UpdateMenuItemDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class MenuItemController {

    private final CreateMenuItemUsecase createMenuItemUsecase;
    private final ReadMenuItemUsecase readMenuItemUsecase;
    private final ReadAllMenuItemUsecase readAllMenuItemUsecase;
    private final UpdateMenuItemUsecase updateMenuItemUsecase;
    private final DeleteMenuItemUsecase deleteMenuItemUsecase;

    public MenuItemDTO createMenuItem(CreateMenuItemDTO createMenuItemDTO)
    {
        return this.createMenuItemUsecase.execute(createMenuItemDTO);
    }

    public MenuItemDTO findById(Long menuItemId)
    {
        return this.readMenuItemUsecase.execute(menuItemId);
    }

    public PaginatedMenuItemDTO findAll(Integer page, Integer perPage)
    {
        return this.readAllMenuItemUsecase.execute(page, perPage);
    }

    public MenuItemDTO updateMenuItem(Long menuItemId, UpdateMenuItemDTO updateMenuItemDTO)
    {
        return this.updateMenuItemUsecase.execute(menuItemId, updateMenuItemDTO);
    }

    public void deleteMenuItem(Long menuItemId)
    {
        this.deleteMenuItemUsecase.execute(menuItemId);
    }
}
