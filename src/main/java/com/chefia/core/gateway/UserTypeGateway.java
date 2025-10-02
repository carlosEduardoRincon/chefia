package com.chefia.core.gateway;

import com.chefia.core.entities.UserType;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserTypeGateway {
    Long save(UserType menuItemToInsert);

    Optional<UserType> findById(Long userTypeId);

    Optional<UserType> findByName(String userTypeName);

    List<UserType> findAll(Pageable pageable);

    void update(Long userTypeId, UserType userTypeEntity);

    void deleteById(Long userTypeId);
}
