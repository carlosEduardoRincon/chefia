package com.chefia.core.port.output.usertype;

import com.chefia.domain.model.UserType;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserTypeRepositoryOutputPort {
    Long save(UserType menuItemToInsert);

    Optional<UserType> findById(Long userTypeId);

    List<UserType> findAll(Pageable pageable);

    void update(Long userTypeId, UserType userTypeEntity);

    void deleteById(Long userTypeId);
}
