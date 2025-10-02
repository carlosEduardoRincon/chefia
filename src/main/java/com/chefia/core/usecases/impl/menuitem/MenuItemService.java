package com.chefia.core.usecases.impl.menuitem;

import com.chefia.core.usecases.interfaces.menuitem.MenuItemInputPort;
import com.chefia.core.gateway.MenuItemGateway;
import com.chefia.core.entities.MenuItem;
import com.chefia.core.exceptions.MenuItemNotFoundException;
import com.chefia.core.mapper.MenuItemMapper;
import com.chefia.menuitems.model.CreateMenuItemDTO;
import com.chefia.menuitems.model.MenuItemDTO;
import com.chefia.menuitems.model.PaginatedMenuItemDTO;
import com.chefia.menuitems.model.UpdateMenuItemDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuItemService implements MenuItemInputPort {

    private final MenuItemGateway menuItemGateway;
    private final MenuItemMapper menuItemMapper;

    public MenuItemService(MenuItemGateway menuItemGateway,
                           MenuItemMapper menuItemMapper
    ) {
        this.menuItemGateway = menuItemGateway;
        this.menuItemMapper = menuItemMapper;
    }

    @Override
    public MenuItemDTO createMenuItem(CreateMenuItemDTO createMenuItemDTO) {
        var menuItemToInsert = this.menuItemMapper.toEntity(createMenuItemDTO);
        this.menuItemGateway.save(menuItemToInsert);

        return this.menuItemMapper.toMenuItemResponseDTO(menuItemToInsert);
    }

    @Override
    public MenuItemDTO findById(Long menuItemId) {
        var menuItem = Optional.ofNullable(this.menuItemGateway
                .findByMenuItemId(menuItemId)
                .orElseThrow(() -> new MenuItemNotFoundException("Menu Item not found with id: " + menuItemId)));
        assert menuItem.isPresent();
        return this.menuItemMapper.toMenuItemResponseDTO(menuItem.get());
    }

    @Override
    public PaginatedMenuItemDTO findAll(Integer page, Integer perPage) {
        Pageable pageable = PageRequest.of(page, perPage);
        List<MenuItem> menuItemPage = this.menuItemGateway.findAll(pageable);

        var menuItemsDto = this.menuItemMapper.toResponseListDTO(menuItemPage);

        return new PaginatedMenuItemDTO()
                .page(page)
                .perPage(perPage)
                .total((long) menuItemPage.size())
                .items(menuItemsDto);
    }

    @Override
    public MenuItemDTO updateMenuItem(Long menuItemId, UpdateMenuItemDTO body) {
        var menuItemEntity = this.menuItemGateway
                .findByMenuItemId(menuItemId)
                .orElseThrow(() -> new MenuItemNotFoundException("Menu Item not found with id: " + menuItemId));

        menuItemEntity.setName(body.getName());
        menuItemEntity.setDescription(body.getName());
        menuItemEntity.setPrice(body.getPrice());
        menuItemEntity.setAvailableOnlyOnSite(body.isAvailableOnlyOnSite());
        menuItemEntity.setImagePath(body.getImagePath());

        this.menuItemGateway.update(menuItemId, menuItemEntity);

        return this.menuItemMapper.toMenuItemResponseDTO(menuItemEntity);
    }

    @Override
    public void deleteMenuItem(Long menuItemId) {
        this.menuItemGateway.deleteById(menuItemId);
    }
}
