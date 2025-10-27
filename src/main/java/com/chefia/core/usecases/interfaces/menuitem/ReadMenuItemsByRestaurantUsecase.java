package com.chefia.core.usecases.interfaces.menuitem;

import com.chefia.menuitems.model.MenuItemDTO;
import java.util.List;

public interface ReadMenuItemsByRestaurantUsecase {
    List<MenuItemDTO> execute(Long restaurantId);
}
