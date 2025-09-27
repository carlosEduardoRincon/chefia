package com.chefia.adapters.usertype.outputs;

import com.chefia.core.port.output.UserTypeRepositoryOutputPort;
import com.chefia.domain.model.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class JdbcUserTypeRepository implements UserTypeRepositoryOutputPort {
    @Override
    public void save(UserType menuItemToInsert) {

    }

    @Override
    public Optional<UserType> findById(Long userTypeId) {
        return Optional.empty();
    }

    @Override
    public Page<UserType> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public void update(Long userTypeId, UserType userTypeEntity) {

    }

    @Override
    public void deleteById(Long userTypeId) {

    }
}
