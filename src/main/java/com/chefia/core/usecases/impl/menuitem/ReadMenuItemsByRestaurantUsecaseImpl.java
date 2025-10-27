package com.chefia.core.usecases.impl.menuitem;

import com.chefia.core.gateway.MenuItemGateway;
import com.chefia.core.mapper.MenuItemMapper;
import com.chefia.core.usecases.interfaces.menuitem.ReadMenuItemsByRestaurantUsecase;
import com.chefia.menuitems.model.MenuItemDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class ReadMenuItemsByRestaurantUsecaseImpl implements ReadMenuItemsByRestaurantUsecase {

    private final MenuItemGateway menuItemGateway;
    private final MenuItemMapper menuItemMapper;

    @Override
    public List<MenuItemDTO> execute(Long restaurantId) {
        return this.menuItemGateway.findByRestaurantId(restaurantId)
                .stream()
                .map(this.menuItemMapper::toMenuItemResponseDTO)
                .toList();
    }
}
