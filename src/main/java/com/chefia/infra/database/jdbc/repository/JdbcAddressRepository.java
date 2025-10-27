package com.chefia.infra.database.jdbc.repository;

import com.chefia.addresses.model.UpdateAddressDTO;
import com.chefia.core.gateway.AddressGateway;
import com.chefia.core.entities.Address;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;
import java.util.Optional;

public class JdbcAddressRepository implements AddressGateway {

    private final JdbcClient jdbcClient;

    public JdbcAddressRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public long saveUserAddress(Address addressToInsert) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql("""
                        INSERT INTO chefia.addresses
                            (street, number, city, state, country, nr_seq_user)
                        VALUES
                            (:street, :number, :city, :state, :country, :nr_seq_user);
                        """)
                .param("street", addressToInsert.getStreet())
                .param("number", addressToInsert.getNumber())
                .param("city", addressToInsert.getCity())
                .param("state", addressToInsert.getState())
                .param("country", addressToInsert.getCountry())
                .param("nr_seq_user", addressToInsert.getUserId())
                .update(keyHolder);

        var keys = keyHolder.getKeys();
        assert keys != null;
        var id = keys.get("nr_seq_address");

        return id != null? ((Number) id).longValue() : null;
    }

    @Override
    public long saveRestaurantAddress(Address addressToInsert) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql("""
                        INSERT INTO chefia.addresses
                            (street, number, city, state, country, nr_seq_restaurant)
                        VALUES
                            (:street, :number, :city, :state, :country, :nr_seq_restaurant);
                        """)
                .param("street", addressToInsert.getStreet())
                .param("number", addressToInsert.getNumber())
                .param("city", addressToInsert.getCity())
                .param("state", addressToInsert.getState())
                .param("country", addressToInsert.getCountry())
                .param("nr_seq_restaurant", addressToInsert.getRestaurantId())
                .update(keyHolder);

        var keys = keyHolder.getKeys();
        assert keys != null;
        var id = keys.get("nr_seq_address");

        return id != null? ((Number) id).longValue() : null;
    }

    @Override
    public Optional<Address> findById(Long id) {
        return jdbcClient.sql("""
                        SELECT * FROM chefia.addresses
                        WHERE nr_seq_address = :id
                        """)
                .param("id", id)
                .query(Address.class)
                .optional();
    }

    @Override
    public List<Address> findByUserId(Long userId) {
        return jdbcClient.sql("""
                        SELECT
                            nr_seq_address,
                            street,
                            number,
                            city,
                            state,
                            country,
                            nr_seq_user AS user_id,
                            nr_seq_restaurant AS restaurant_id
                        FROM chefia.addresses
                        WHERE nr_seq_user = :userId
                        """)
                .param("userId", userId)
                .query(Address.class)
                .list();
    }

    @Override
    public List<Address> findByRestaurantId(Long restaurantId) {
        return jdbcClient.sql("""
                        SELECT
                            nr_seq_address,
                            street,
                            number,
                            city,
                            state,
                            country,
                            nr_seq_user AS user_id,
                            nr_seq_restaurant AS restaurant_id
                        FROM chefia.addresses
                        WHERE nr_seq_restaurant = :restaurantId
                        """)
                .param("restaurantId", restaurantId)
                .query(Address.class)
                .list();
    }

    @Override
    public void updateAddress(Long id, UpdateAddressDTO updateAddressDTO) {
        jdbcClient.sql("""
                            UPDATE chefia.addresses
                            SET street = :street,
                                number = :number,
                                city = :city,
                                state = :state,
                                country = :country
                            WHERE nr_seq_address = :id
                        """)
                .param("id", id)
                .param("street", updateAddressDTO.getStreet())
                .param("number", updateAddressDTO.getNumber())
                .param("city", updateAddressDTO.getCity())
                .param("state", updateAddressDTO.getState())
                .param("country", updateAddressDTO.getCountry())
                .update();
    }

    @Override
    public void deleteById(Long id) {
        jdbcClient.sql("""
                            DELETE FROM chefia.addresses
                            WHERE nr_seq_address = :id
                        """)
                .param("id", id)
                .update();
    }
}
