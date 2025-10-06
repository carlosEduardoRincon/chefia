package com.chefia.core.usecases.interfaces.menuitem;

import com.chefia.menuitems.model.PaginatedMenuItemDTO;

public interface ReadAllMenuItemUsecase {
    PaginatedMenuItemDTO execute(Integer page, Integer perPage);
}
