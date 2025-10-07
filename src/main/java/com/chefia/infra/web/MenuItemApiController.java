package com.chefia.infra.web;

import com.chefia.core.controllers.MenuItemController;
import com.chefia.menuitems.api.MenuitemApi;
import com.chefia.menuitems.model.CreateMenuItemDTO;
import com.chefia.menuitems.model.MenuItemDTO;
import com.chefia.menuitems.model.PaginatedMenuItemDTO;
import com.chefia.menuitems.model.UpdateMenuItemDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class MenuItemApiController implements MenuitemApi {

    private final MenuItemController menuItemController;

    @Override
    public ResponseEntity<MenuItemDTO> createMenuItem(CreateMenuItemDTO body)
    {
        log.info("[POST] - Create Menu Item");
        var createdMenuItem = this.menuItemController.createMenuItem(body);
        return ResponseEntity.status(201).body(createdMenuItem);
    }

    @Override
    public ResponseEntity<MenuItemDTO> getMenuItem(Long menuItemId)
    {
        log.info("[GET] - Get Menu Item with ID: {}", menuItemId);
        var getMenuItem = this.menuItemController.findById(menuItemId);
        return ResponseEntity.ok(getMenuItem);
    }

    @Override
    public ResponseEntity<List<MenuItemDTO>> getMenuItemsByRestaurant(Long restaurantId)
    {
        log.info("[GET] - List Menu Items for Restaurant ID: {}", restaurantId);
        var getMenuItem = this.menuItemController.findByRestaurantId(restaurantId);
        return ResponseEntity.ok(getMenuItem);
    }

    @Override
    public ResponseEntity<PaginatedMenuItemDTO> listMenuItems(Integer page, Integer perPage)
    {
        log.info("[GET] - List Menu Items");
        var listAllMenuItems = this.menuItemController.findAll(page, perPage);
        return ResponseEntity.ok(listAllMenuItems);
    }

    @Override
    public ResponseEntity<MenuItemDTO> updateMenuItem(Long menuItemId, UpdateMenuItemDTO body)
    {
        log.info("[PUT] - Update Menu Item");
        var updatedMenuItem = this.menuItemController.updateMenuItem(menuItemId, body);
        return ResponseEntity.ok().body(updatedMenuItem);
    }

    @Override
    public ResponseEntity<Void> deleteMenuItem(Long menuItemId)
    {
        log.info("[DELETE] - Remove Menu Item with ID: {}", menuItemId);
        this.menuItemController.deleteMenuItem(menuItemId);
        return ResponseEntity.noContent().build();
    }
}
