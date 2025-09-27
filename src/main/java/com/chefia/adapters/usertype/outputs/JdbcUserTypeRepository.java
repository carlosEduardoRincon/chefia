package com.chefia.adapters.usertype.outputs;

import com.chefia.core.port.output.UserTypeRepositoryOutputPort;
import com.chefia.domain.model.User;
import com.chefia.domain.model.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcUserTypeRepository implements UserTypeRepositoryOutputPort {

    private final JdbcClient jdbcClient;

    public JdbcUserTypeRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void save(UserType userType) {
        jdbcClient.sql("""
                INSERT INTO user_types
                    (name, description, active, created_at)
                VALUES
                    (:name, :description, :active, :createdAt)
                """)
                .param("name", userType.getName())
                .param("description", userType.getDescription())
                .param("active", userType.getActive())
                .param("createdAt", LocalDateTime.now())
                .update();
    }

    @Override
    public Optional<UserType> findById(Long userTypeId) {
        return jdbcClient.sql("""
                SELECT * FROM user_types
                WHERE nr_seq_user_type = :id
                """)
                .param("id", userTypeId)
                .query(UserType.class)
                .optional();
    }

    @Override
    public List<UserType> findAll(Pageable pageable) {
        return jdbcClient.sql("""
                SELECT * FROM user_types
                """)
                .query(UserType.class)
                .list();
    }

    @Override
    public void update(Long userTypeId, UserType userType) {
        jdbcClient.sql("""
                UPDATE user_types
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
                DELETE FROM user_types
                WHERE nr_seq_user_type = :id
                """)
                .param("id", userTypeId)
                .update();
    }
}
