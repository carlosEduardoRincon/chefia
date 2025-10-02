package com.chefia.infra.database.jdbc.repository;

import com.chefia.addresses.model.UpdateAddressDTO;
import com.chefia.core.gateway.AddressGateway;
import com.chefia.core.entities.Address;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.Optional;

public class JdbcAddressRepository implements AddressGateway {

    private final JdbcClient jdbcClient;

    public JdbcAddressRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public long saveAddressForUser(Address addressToInsert) {
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
    public long saveAddressForRestaurant(Address addressToInsert) {
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
    public Optional<Address> findById(Long addressId) {
        return jdbcClient.sql("""
                        SELECT * FROM chefia.addresses
                        WHERE nr_seq_address = :id
                        """)
                .param("id", addressId)
                .query(Address.class)
                .optional();
    }

    @Override
    public void updateAddress(Long addressId, UpdateAddressDTO updateAddressDTO) {
        jdbcClient.sql("""
                            UPDATE chefia.addresses
                            SET street = :street,
                                number = :number,
                                city = :city,
                                state = :state,
                                country = :country
                            WHERE nr_seq_address = :id
                        """)
                .param("id", addressId)
                .param("street", updateAddressDTO.getStreet())
                .param("number", updateAddressDTO.getNumber())
                .param("city", updateAddressDTO.getCity())
                .param("state", updateAddressDTO.getState())
                .param("country", updateAddressDTO.getCountry())
                .update();
    }

    @Override
    public void deleteById(Long addressId) {
        jdbcClient.sql("""
                            DELETE FROM chefia.addresses
                            WHERE nr_seq_address = :id
                        """)
                .param("id", addressId)
                .update();
    }
}
