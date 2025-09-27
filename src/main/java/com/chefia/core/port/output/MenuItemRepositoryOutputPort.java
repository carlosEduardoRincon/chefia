package com.chefia.core.port.output;

import com.chefia.domain.model.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MenuItemRepositoryOutputPort {

    void save(MenuItem menuItemToInsert);

    Optional<MenuItem> findById(Long menuItemId);

    Page<MenuItem> findAll(Pageable pageable);

    void update(Long menuItemId, MenuItem menuItemEntity1);

    void deleteById(Long menuItemId);
}
