package com.chefia.core.port.output;

import com.chefia.domain.model.User;
import com.chefia.users.model.CreateUserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryOutputPort {

    void save(User userToInsert);

    Optional<User> findById(Long userId);

    UserDetails findByLogin(String username);

    List<User> findAll(Pageable pageable);

    void update(Long userId, User userEntity);

    void deleteById(Long id);

    void updateUserStatus(Long userId, User userEntity);

    void updateUserPassword(Long userId, User userEntity);
}
