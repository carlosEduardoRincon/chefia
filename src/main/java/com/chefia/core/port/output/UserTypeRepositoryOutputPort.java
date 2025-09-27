package com.chefia.core.port.output;

import com.chefia.domain.model.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserTypeRepositoryOutputPort {
    void save(UserType menuItemToInsert);

    Optional<UserType> findById(Long userTypeId);

    Page<UserType> findAll(Pageable pageable);

    void update(Long userTypeId, UserType userTypeEntity);

    void deleteById(Long userTypeId);
}
