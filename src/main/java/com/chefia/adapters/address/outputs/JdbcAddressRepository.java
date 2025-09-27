package com.chefia.adapters.address.outputs;

import com.chefia.addresses.model.UpdateAddressDTO;
import com.chefia.core.port.output.AddressRepositoryOutputPort;
import com.chefia.domain.model.Address;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.Optional;

public class JdbcAddressRepository implements AddressRepositoryOutputPort {

    private final JdbcClient jdbcClient;

    public JdbcAddressRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void saveAddressForUser(Address addressToInsert) {
        jdbcClient.sql("""
                        INSERT INTO addresses
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
                .update();
    }

    @Override
    public void saveAddressForRestaurant(Address addressToInsert) {
        jdbcClient.sql("""
                        INSERT INTO addresses
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
                .update();
    }

    @Override
    public Optional<Address> findById(Long addressId) {
        return jdbcClient.sql("""
                        SELECT * FROM addresses
                        WHERE id = :id
                        """)
                .param("id", addressId)
                .query(Address.class)
                .optional();
    }

    @Override
    public void updateAddress(Long addressId, UpdateAddressDTO updateAddressDTO) {
        jdbcClient.sql("""
                            UPDATE addresses
                            SET street = :street,
                                number = :number,
                                city = :city,
                                state = :state,
                                country = :country
                            WHERE id = :id
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
                            DELETE FROM addresses
                            WHERE id = :id
                        """)
                .param("id", addressId)
                .update();
    }
}
