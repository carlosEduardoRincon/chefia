package com.chefia.core.port.output;

import com.chefia.domain.model.MenuItem;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepositoryOutputPort {

    void save(MenuItem menuItemToInsert);

    Optional<MenuItem> findByMenuItemId(Long menuItemId);

    List<MenuItem> findByRestaurantId(Long restaurantId);

    List<MenuItem> findAll(Pageable pageable);

    void update(Long menuItemId, MenuItem menuItemEntity1);

    void deleteById(Long menuItemId);
}
