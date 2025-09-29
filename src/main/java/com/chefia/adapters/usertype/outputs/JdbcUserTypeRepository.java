package com.chefia.adapters.usertype.outputs;

import com.chefia.core.port.output.usertype.UserTypeRepositoryOutputPort;
import com.chefia.domain.model.UserType;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JdbcUserTypeRepository implements UserTypeRepositoryOutputPort {

    private final JdbcClient jdbcClient;

    public JdbcUserTypeRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Long save(UserType userType) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql("""
                INSERT INTO chefia.user_types
                    (name, description, active, created_at)
                VALUES
                    (:name, :description, :active, :createdAt)
                """)
                .param("name", userType.getName())
                .param("description", userType.getDescription())
                .param("active", userType.getActive())
                .param("createdAt", userType.getCreatedAt())
                .update(keyHolder);

        var keys = keyHolder.getKeys();
        assert keys != null;
        var id = keys.get("nr_seq_user_type");

        return id != null? ((Number) id).longValue() : null;
    }

    @Override
    public Optional<UserType> findById(Long userTypeId) {
        return jdbcClient.sql("""
                SELECT * FROM chefia.user_types
                WHERE nr_seq_user_type = :id
                """)
                .param("id", userTypeId)
                .query(UserType.class)
                .optional();
    }

    @Override
    public Optional<UserType> findByName(String userTypeName) {
        return jdbcClient.sql("""
                SELECT * FROM chefia.user_types
                WHERE name = :name
                """)
                .param("name", userTypeName)
                .query(UserType.class)
                .optional();
    }

    @Override
    public List<UserType> findAll(Pageable pageable) {
        return jdbcClient.sql("""
                SELECT * FROM chefia.user_types
                """)
                .query(UserType.class)
                .list();
    }

    @Override
    public void update(Long userTypeId, UserType userType) {
        jdbcClient.sql("""
                UPDATE chefia.user_types
                SET name = :name,
                    description = :description,
                    active = :active,
                    updated_at = :updatedAt
                WHERE nr_seq_user_type = :id
                """)
                .param("id", userTypeId)
                .param("name", userType.getName())
                .param("description", userType.getDescription())
                .param("active", userType.getActive())
                .param("updatedAt", LocalDateTime.now())
                .update();
    }

    @Override
    public void deleteById(Long userTypeId) {
        jdbcClient.sql("""
                DELETE FROM chefia.user_types
                WHERE nr_seq_user_type = :id
                """)
                .param("id", userTypeId)
                .update();
    }
}
