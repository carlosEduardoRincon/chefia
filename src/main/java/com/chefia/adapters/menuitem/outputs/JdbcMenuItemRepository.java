package com.chefia.adapters.menuitem.outputs;

import com.chefia.core.port.output.MenuItemRepositoryOutputPort;
import com.chefia.domain.model.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class JdbcMenuItemRepository implements MenuItemRepositoryOutputPort {
    @Override
    public void save(MenuItem menuItemToInsert) {

    }

    @Override
    public Optional<MenuItem> findById(Long menuItemId) {
        return Optional.empty();
    }

    @Override
    public Page<MenuItem> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public void update(Long menuItemId, MenuItem menuItemEntity1) {

    }

    @Override
    public void deleteById(Long menuItemId) {

    }
}
