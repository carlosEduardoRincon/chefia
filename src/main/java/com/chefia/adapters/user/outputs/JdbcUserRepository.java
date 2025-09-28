package com.chefia.adapters.user.outputs;

import com.chefia.core.port.output.user.UserRepositoryOutputPort;
import com.chefia.domain.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
    public long save(User userToInsert) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql("""
                        INSERT INTO chefia.users
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
                .update(keyHolder);

        var keys = keyHolder.getKeys();
        assert keys != null;
        var id = keys.get("nr_seq_user");

        return id != null? ((Number) id).longValue() : null;
    }

    @Override
    public Optional<User> findById(Long userId) {
        return jdbcClient.sql("""
                        SELECT * FROM chefia.users
                        WHERE nr_seq_user = :id
                        """)
                .param("id", userId)
                .query(User.class)
                .optional();
    }

    @Override
    public Optional<User> findByLogin(String username) {
        return jdbcClient.sql("""
                        SELECT * FROM chefia.users
                        WHERE login = :login
                        """)
                .param("login", username)
                .query(User.class)
                .optional();
    }

    @Override
    public List<User> findAll(Pageable pageable) {
        return jdbcClient.sql("""
                         SELECT * FROM chefia.users LIMIT :size OFFSET :offset
                        """)
                .param("size", pageable.getPageSize())
                .param("offset", pageable.getOffset())
                .query(User.class)
                .list();
    }

    @Override
    public void update(Long userId, User userEntity) {
        jdbcClient.sql("""
                            UPDATE chefia.users
                            SET name = :name,
                                email = :email,
                                login = :login,
                                updated_at = :updatedAt
                            WHERE nr_seq_user = :id
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
                            DELETE FROM chefia.users
                            WHERE nr_seq_user = :id
                        """)
                .param("id", userId)
                .update();
    }

    @Override
    public void updateUserStatus(Long userId, User userEntity) {
        jdbcClient.sql("""
                            UPDATE chefia.users
                            SET active = :active
                            WHERE nr_seq_user = :id
                        """)
                .param("id", userId)
                .param("active", userEntity.isActive())
                .update();
    }

    @Override
    public void updateUserPassword(Long userId, User userEntity) {
        jdbcClient.sql("""
                            UPDATE chefia.users
                            SET password = :password
                            WHERE nr_seq_user = :id
                        """)
                .param("id", userId)
                .param("password", userEntity.getPassword())
                .update();
    }

    @Override
    public boolean findByEmailValidation(String email) {
        var query = """
                SELECT COUNT(1)
                FROM chefia.users
                WHERE email = :email
                """;

        var count = jdbcClient.sql(query)
                .param("email", email)
                .query(Integer.class)
                .single();
        return count > 0;
    }

    @Override
    public boolean findByLoginValidation(String login) {
        var query = """
                SELECT COUNT(1)
                FROM chefia.users
                WHERE login = :login
                """;

        var count = jdbcClient.sql(query)
                .param("login", login)
                .query(Integer.class)
                .single();
        return count > 0;
    }

}
