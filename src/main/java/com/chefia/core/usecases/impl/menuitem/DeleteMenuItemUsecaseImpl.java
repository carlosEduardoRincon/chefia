package com.chefia.core.usecases.impl.menuitem;

import com.chefia.core.gateway.MenuItemGateway;
import com.chefia.core.usecases.interfaces.menuitem.DeleteMenuItemUsecase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class DeleteMenuItemUsecaseImpl implements DeleteMenuItemUsecase {

    private final MenuItemGateway menuItemGateway;

    @Override
    public void execute(Long menuItemId) {
        this.menuItemGateway.deleteById(menuItemId);
    }
}
