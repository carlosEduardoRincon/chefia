package com.chefia.infra.database.jdbc.repository;

import com.chefia.core.gateway.MenuItemGateway;
import com.chefia.core.entities.MenuItem;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;
import java.util.Optional;

public class JdbcMenuItemRepository implements MenuItemGateway {

    private final JdbcClient jdbcClient;

    public JdbcMenuItemRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public long save(MenuItem item) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql("""
                        INSERT INTO chefia.menu_items
                            (name, description, price, available_only_on_site, image_path, nr_seq_restaurant)
                        VALUES
                            (:name, :description, :price, :availableOnlyOnSite, :imagePath, :restaurantId)
                        """)
                .param("name", item.getName())
                .param("description", item.getDescription())
                .param("price", item.getPrice())
                .param("availableOnlyOnSite", item.getAvailableOnlyOnSite())
                .param("imagePath", item.getImagePath())
                .param("restaurantId", item.getRestaurantId())
                .update(keyHolder);

        var keys = keyHolder.getKeys();
        assert keys != null;
        var restaurantId = keys.get("nr_seq_menu_item");

        return restaurantId != null ? ((Number) restaurantId).longValue() : null;
    }

    @Override
    public Optional<MenuItem> findByMenuItemId(Long menuItemId) {
        return jdbcClient.sql("""
                        SELECT
                         nr_seq_menu_item,
                         name,
                         description,
                         price,
                         available_only_on_site,
                         image_path,
                        nr_seq_restaurant AS restaurant_id
                        FROM chefia.menu_items
                        WHERE nr_seq_menu_item = :id
                        """)
                .param("id", menuItemId)
                .query(MenuItem.class)
                .optional();
    }

    @Override
    public List<MenuItem> findByRestaurantId(Long restaurantId) {
        return jdbcClient.sql("""
                        SELECT * FROM chefia.menu_items
                        WHERE nr_seq_restaurant = :restaurantId
                        """)
                .param("restaurantId", restaurantId)
                .query(MenuItem.class)
                .list();
    }

    @Override
    public List<MenuItem> findAll(Pageable pageable) {
        return jdbcClient.sql("""
                        SELECT
                         nr_seq_menu_item,
                         name,
                         description,
                         price,
                         available_only_on_site,
                         image_path,
                         nr_seq_restaurant AS restaurant_id
                        FROM chefia.menu_items
                        LIMIT :size OFFSET :offset
                        """)
                .param("size", pageable.getPageSize())
                .param("offset", pageable.getOffset())
                .query(MenuItem.class)
                .list();
    }

    @Override
    public void update(Long menuItemId, MenuItem item) {
        jdbcClient.sql("""
                        UPDATE chefia.menu_items
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
                        DELETE FROM chefia.menu_items
                        WHERE nr_seq_menu_item = :id
                        """)
                .param("id", menuItemId)
                .update();
    }
}