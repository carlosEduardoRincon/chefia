package com.chefia.core.gateway;

import com.chefia.core.entities.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserGateway {

    long save(User userToInsert);

    Optional<User> findById(Long userId);

    Optional<User> findByLogin(String username);

    List<User> findAll(Pageable pageable);

    void update(Long userId, User userEntity);

    void deleteById(Long id);

    void updateUserStatus(Long userId, User userEntity);

    void updateUserPassword(Long userId, User userEntity);

    boolean findByEmailValidation(String email);

    boolean findByLoginValidation(String login);

}
