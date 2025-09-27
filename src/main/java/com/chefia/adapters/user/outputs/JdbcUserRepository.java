package com.chefia.adapters.user.outputs;

import com.chefia.core.port.output.UserRepositoryOutputPort;
import com.chefia.domain.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class JdbcUserRepository implements UserRepositoryOutputPort {

    private final JdbcClient jdbcClient;

    public JdbcUserRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void save(User userToInsert) {
        jdbcClient.sql("""
                        INSERT INTO users
                            (name, email, login, password, active, created_at, updated_at, user_type_id)
                        VALUES
                            (:name, :email, :login, :password, :active, :created_at, :updated_at, :user_type_id);
                        """)
                .param("name", userToInsert.getName())
                .param("email", userToInsert.getEmail())
                .param("login", userToInsert.getLogin())
                .param("password", userToInsert.getPassword())
                .param("active", userToInsert.isActive())
                .param("created_at", userToInsert.getCreatedAt())
                .param("updated_at", userToInsert.getUpdatedAt())
                .param("user_type_id", userToInsert.getUserTypeId())
                .update();
    }

    @Override
    public Optional<User> findById(Long userId) {
        return jdbcClient.sql("""
                        SELECT * FROM users
                        WHERE id = :id
                        """)
                .param("id", userId)
                .query(User.class)
                .optional();
    }

    @Override
    public UserDetails findByLogin(String username) {
        return jdbcClient.sql("""
                        SELECT * FROM users
                        WHERE login = :login
                        """)
                .param("login", username)
                .query(UserDetails.class)
                .optional()
                .get();
    }

    @Override
    public List<User> findAll(Pageable pageable) {
        return jdbcClient.sql("""
                         SELECT * FROM users LIMIT :size OFFSET :offset
                        """)
                .param("size", pageable.getPageSize())
                .param("offset", pageable.getOffset())
                .query(User.class)
                .list();
    }

    @Override
    public void update(Long userId, User userEntity) {
        jdbcClient.sql("""
                            UPDATE users
                            SET name = :name,
                                email = :email,
                                login = :login,
                                updatedAt = :updatedAt
                            WHERE id = :id
                        """)
                .param("id", userId)
                .param("name", userEntity.getName())
                .param("email", userEntity.getEmail())
                .param("login", userEntity.getLogin())
                .param("updatedAt", LocalDateTime.now())
                .update();
    }

    @Override
    public void deleteById(Long userId) {
        jdbcClient.sql("""
                            DELETE FROM users
                            WHERE id = :id
                        """)
                .param("id", userId)
                .update();
    }

    @Override
    public void updateUserStatus(Long userId, User userEntity) {
        jdbcClient.sql("""
                            UPDATE users
                            SET active = :active
                            WHERE id = :id
                        """)
                .param("id", userId)
                .param("active", userEntity.isActive())
                .update();
    }

    @Override
    public void updateUserPassword(Long userId, User userEntity) {
        jdbcClient.sql("""
                            UPDATE users
                            SET password = :password
                            WHERE id = :id
                        """)
                .param("id", userId)
                .param("password", userEntity.getPassword())
                .update();
    }
}
