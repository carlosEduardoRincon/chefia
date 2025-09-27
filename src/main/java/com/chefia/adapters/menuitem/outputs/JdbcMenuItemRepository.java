package com.chefia.adapters.menuitem.outputs;

import com.chefia.core.port.output.MenuItemRepositoryOutputPort;
import com.chefia.domain.model.MenuItem;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcMenuItemRepository implements MenuItemRepositoryOutputPort {

    private final JdbcClient jdbcClient;

    public JdbcMenuItemRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void save(MenuItem item) {
        jdbcClient.sql("""
                        INSERT INTO menu_items
                            (name, description, price, available_only_on_site, image_path, restaurant_id)
                        VALUES
                            (:name, :description, :price, :availableOnlyOnSite, :imagePath, :restaurantId)
                        """)
                .param("name", item.getName())
                .param("description", item.getDescription())
                .param("price", item.getPrice())
                .param("availableOnlyOnSite", item.getAvailableOnlyOnSite())
                .param("imagePath", item.getImagePath())
                .param("restaurantId", item.getRestaurantId())
                .update();
    }

    @Override
    public Optional<MenuItem> findByMenuItemId(Long menuItemId) {
        return jdbcClient.sql("""
                        SELECT * FROM menu_items
                        WHERE nr_seq_menu_item = :id
                        """)
                .param("id", menuItemId)
                .query(MenuItem.class)
                .optional();
    }

    @Override
    public List<MenuItem> findByRestaurantId(Long restaurantId) {
        return jdbcClient.sql("""
                        SELECT * FROM menu_items
                        WHERE restaurant_id = :restaurantId
                        """)
                .param("restaurantId", restaurantId)
                .query(MenuItem.class)
                .list();
    }

    @Override
    public List<MenuItem> findAll(Pageable pageable) {
        return jdbcClient.sql("""
                         SELECT * FROM menu_items LIMIT :size OFFSET :offset
                        """)
                .param("size", pageable.getPageSize())
                .param("offset", pageable.getOffset())
                .query(MenuItem.class)
                .list();
    }

    @Override
    public void update(Long menuItemId, MenuItem item) {
        jdbcClient.sql("""
                        UPDATE menu_items
                        SET name = :name,
                            description = :description,
                            price = :price,
                            available_only_on_site = :availableOnlyOnSite,
                            image_path = :imagePath
                        WHERE nr_seq_menu_item = :id
                        """)
                .param("id", menuItemId)
                .param("name", item.getName())
                .param("description", item.getDescription())
                .param("price", item.getPrice())
                .param("availableOnlyOnSite", item.getAvailableOnlyOnSite())
                .param("imagePath", item.getImagePath())
                .update();
    }

    @Override
    public void deleteById(Long menuItemId) {
        jdbcClient.sql("""
                        DELETE FROM menu_items
                        WHERE nr_seq_menu_item = :id
                        """)
                .param("id", menuItemId)
                .update();
    }
}