package com.chefia.core.usecases.impl.menuitem;

import com.chefia.core.entities.MenuItem;
import com.chefia.core.gateway.MenuItemGateway;
import com.chefia.core.mapper.MenuItemMapper;
import com.chefia.core.usecases.interfaces.menuitem.ReadAllMenuItemUsecase;
import com.chefia.menuitems.model.PaginatedMenuItemDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class ReadAllMenuItemUsecaseImpl implements ReadAllMenuItemUsecase {

    private final MenuItemGateway menuItemGateway;
    private final MenuItemMapper menuItemMapper;

    @Override
    public PaginatedMenuItemDTO execute(Integer page, Integer perPage) {
        Pageable pageable = PageRequest.of(page, perPage);
        List<MenuItem> menuItemPage = this.menuItemGateway.findAll(pageable);

        var menuItemsDto = this.menuItemMapper.toResponseListDTO(menuItemPage);

        return new PaginatedMenuItemDTO()
                .page(page)
                .perPage(perPage)
                .total((long) menuItemPage.size())
                .items(menuItemsDto);
    }
}
