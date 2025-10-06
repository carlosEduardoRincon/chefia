package com.chefia.core.gateway;

import com.chefia.core.entities.MenuItem;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MenuItemGateway {

    long save(MenuItem menuItemToInsert);

    Optional<MenuItem> findByMenuItemId(Long menuItemId);

    List<MenuItem> findByRestaurantId(Long restaurantId);

    List<MenuItem> findAll(Pageable pageable);

    void update(Long menuItemId, MenuItem menuItemEntity1);

    void deleteById(Long menuItemId);
}
