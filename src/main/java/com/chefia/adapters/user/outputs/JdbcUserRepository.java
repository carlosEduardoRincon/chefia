package com.chefia.adapters.user.outputs;

import com.chefia.core.port.output.UserRepositoryOutputPort;
import com.chefia.domain.model.User;
import com.chefia.users.model.CreateUserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class JdbcUserRepository implements UserRepositoryOutputPort {
    @Override
    public void save(User userToInsert) {

    }

    @Override
    public void save(CreateUserDTO userToInsert) {

    }

    @Override
    public Optional<User> findById(Long userId) {
        return Optional.empty();
    }

    @Override
    public UserDetails findByLogin(String username) {
        return null;
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public void update(Long userId, User userEntity) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
