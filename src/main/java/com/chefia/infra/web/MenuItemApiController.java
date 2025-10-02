package com.chefia.infra.web;

import com.chefia.core.usecases.interfaces.menuitem.MenuItemInputPort;
import com.chefia.menuitems.api.MenuitemApi;
import com.chefia.menuitems.model.CreateMenuItemDTO;
import com.chefia.menuitems.model.MenuItemDTO;
import com.chefia.menuitems.model.PaginatedMenuItemDTO;
import com.chefia.menuitems.model.UpdateMenuItemDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MenuItemApiController implements MenuitemApi {

    private final MenuItemInputPort menuItemInputPort;

    public MenuItemApiController(MenuItemInputPort menuItemInputPort) {
        this.menuItemInputPort = menuItemInputPort;
    }

    @Override
    public ResponseEntity<MenuItemDTO> createMenuItem(CreateMenuItemDTO body)
    {
        log.info("[POST] - Create Menu Item");
        var createdMenuItem = this.menuItemInputPort.createMenuItem(body);
        return ResponseEntity.status(201).body(createdMenuItem);
    }

    @Override
    public ResponseEntity<MenuItemDTO> getMenuItem(Long menuItemId)
    {
        log.info("[GET] - List Menu Item");
        var getMenuItem = this.menuItemInputPort.findById(menuItemId);
        return ResponseEntity.ok(getMenuItem);
    }

    @Override
    public ResponseEntity<PaginatedMenuItemDTO> listMenuItems(Integer page, Integer perPage)
    {
        log.info("[GET] - List Menu Items");
        var listAllMenuItems = this.menuItemInputPort.findAll(page, perPage);
        return ResponseEntity.ok(listAllMenuItems);
    }

    @Override
    public ResponseEntity<MenuItemDTO> updateMenuItem(Long menuItemId, UpdateMenuItemDTO body)
    {
        log.info("[PUT] - Update Address");
        var updatedMenuItem = this.menuItemInputPort.updateMenuItem(menuItemId, body);
        return ResponseEntity.ok().body(updatedMenuItem);
    }

    @Override
    public ResponseEntity<Void> deleteMenuItem(Long menuItemId)
    {
        log.info("[DELETE] - Remove Menu Item");
        this.menuItemInputPort.deleteMenuItem(menuItemId);
        return ResponseEntity.ok().build();
    }
}
