package com.chefia.core.service;

import com.chefia.core.port.input.MenuItemInputPort;
import com.chefia.core.port.output.MenuItemRepositoryOutputPort;
import com.chefia.domain.model.MenuItem;
import com.chefia.infra.exception.MenuItemNotFoundException;
import com.chefia.infra.exception.UserNotFoundException;
import com.chefia.infra.mapper.MenuItemMapper;
import com.chefia.menuitems.model.CreateMenuItemDTO;
import com.chefia.menuitems.model.MenuItemDTO;
import com.chefia.menuitems.model.PaginatedMenuItemDTO;
import com.chefia.menuitems.model.UpdateMenuItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MenuItemService implements MenuItemInputPort {

    private final MenuItemRepositoryOutputPort menuItemRepositoryOutputPort;
    private final MenuItemMapper menuItemMapper;

    public MenuItemService(MenuItemRepositoryOutputPort menuItemRepositoryOutputPort,
                           MenuItemMapper menuItemMapper
    ) {
        this.menuItemRepositoryOutputPort = menuItemRepositoryOutputPort;
        this.menuItemMapper = menuItemMapper;
    }

    @Override
    public MenuItemDTO createMenuItem(CreateMenuItemDTO createMenuItemDTO) {
        var menuItemToInsert = this.menuItemMapper.toEntity(createMenuItemDTO);
        this.menuItemRepositoryOutputPort.save(menuItemToInsert);

        return this.menuItemMapper.toMenuItemResponseDTO(menuItemToInsert);
    }

    @Override
    public MenuItemDTO findById(Long menuItemId) {
        var menuItem = Optional.ofNullable(this.menuItemRepositoryOutputPort
                .findById(menuItemId)
                .orElseThrow(() -> new MenuItemNotFoundException("Menu Item not found with id: " + menuItemId)));
        assert menuItem.isPresent();
        return this.menuItemMapper.toMenuItemResponseDTO(menuItem.get());
    }

    @Override
    public PaginatedMenuItemDTO findAll(Integer page, Integer perPage) {
        Pageable pageable = PageRequest.of(page, perPage);
        Page<MenuItem> menuItemPage = this.menuItemRepositoryOutputPort.findAll(pageable);

        var menuItemsDto = this.menuItemMapper.toResponseListDTO(menuItemPage.getContent());

        return new PaginatedMenuItemDTO()
                .page(page)
                .perPage(perPage)
                .total(menuItemPage.getTotalElements())
                .items(menuItemsDto);
    }

    @Override
    public MenuItemDTO updateMenuItem(Long menuItemId, UpdateMenuItemDTO body) {
        var menuItemEntity = this.menuItemRepositoryOutputPort
                .findById(menuItemId)
                .orElseThrow(() -> new MenuItemNotFoundException("Menu Item not found with id: " + menuItemId));

        menuItemEntity.setName(body.getName());
        menuItemEntity.setDescription(body.getName());
        menuItemEntity.setPrice(body.getPrice());
        menuItemEntity.setAvailableOnlyOnSite(body.isAvailableOnlyOnSite());
        menuItemEntity.setImagePath(body.getImagePath());

        this.menuItemRepositoryOutputPort.update(menuItemId, menuItemEntity);

        return this.menuItemMapper.toMenuItemResponseDTO(menuItemEntity);
    }

    @Override
    public void deleteMenuItem(Long menuItemId) {
        this.menuItemRepositoryOutputPort.deleteById(menuItemId);
    }
}
