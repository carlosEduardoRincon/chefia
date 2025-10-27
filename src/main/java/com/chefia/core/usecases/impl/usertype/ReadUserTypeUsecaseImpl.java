package com.chefia.core.usecases.impl.usertype;

import com.chefia.core.exceptions.UserTypeNotFoundException;
import com.chefia.core.gateway.UserTypeGateway;
import com.chefia.core.mapper.UserTypeMapper;
import com.chefia.core.usecases.interfaces.usertype.ReadUserTypeUsecase;
import com.chefia.usertypes.model.UserTypeDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class ReadUserTypeUsecaseImpl implements ReadUserTypeUsecase {

    private final UserTypeGateway userTypeGateway;
    private final UserTypeMapper userTypeMapper;

    @Override
    public UserTypeDTO execute(Long userTypeId) {
        var userType = Optional.ofNullable(this.userTypeGateway
                .findById(userTypeId)
                .orElseThrow(() -> new UserTypeNotFoundException("User Type not found with id: " + userTypeId)));
        assert userType.isPresent();
        return this.userTypeMapper.toUserTypeResponseDTO(userType.get());
    }
}
