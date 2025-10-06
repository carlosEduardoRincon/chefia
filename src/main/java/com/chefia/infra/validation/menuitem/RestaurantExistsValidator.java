package com.chefia.infra.validation.menuitem;

import com.chefia.core.entities.MenuItem;
import com.chefia.core.exceptions.RestaurantNotFoundException;
import com.chefia.core.gateway.MenuItemValidatorGateway;
import com.chefia.core.gateway.RestaurantGateway;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class RestaurantExistsValidator implements MenuItemValidatorGateway {

    private final RestaurantGateway restaurantGateway;

    @Override
    public void validate(MenuItem menuItem) {
        if(this.restaurantGateway.findById(menuItem.getRestaurantId()).isEmpty()) {
            throw new RestaurantNotFoundException("Restaurant does not exist");
        }
    }
}
