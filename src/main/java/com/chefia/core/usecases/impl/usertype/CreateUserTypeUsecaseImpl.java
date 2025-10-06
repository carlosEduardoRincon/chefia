package com.chefia.core.usecases.impl.usertype;

import com.chefia.core.entities.UserType;
import com.chefia.core.gateway.UserTypeGateway;
import com.chefia.core.gateway.UserTypeValidatorGateway;
import com.chefia.core.mapper.UserTypeMapper;
import com.chefia.core.usecases.interfaces.usertype.CreateUserTypeUsecase;
import com.chefia.usertypes.model.CreateUserTypeDTO;
import com.chefia.usertypes.model.UserTypeDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class CreateUserTypeUsecaseImpl implements CreateUserTypeUsecase {

    private final UserTypeGateway userTypeGateway;
    private final List<UserTypeValidatorGateway> userTypeValidatorGateways;
    private final UserTypeMapper userTypeMapper;

    @Override
    public UserTypeDTO execute(CreateUserTypeDTO createUserTypeDTO) {
        var userTypeToInsert = this.userTypeMapper.toEntity(createUserTypeDTO);
        this.validateUserType(userTypeToInsert);

        var userTypeId = this.userTypeGateway.save(userTypeToInsert);

        userTypeToInsert.setNrSeqUserType(userTypeId);

        return this.userTypeMapper.toUserTypeResponseDTO(userTypeToInsert);
    }

    private void validateUserType(UserType userType) {
        for (UserTypeValidatorGateway userValidator : userTypeValidatorGateways) {
            userValidator.validate(userType);
        }
    }
}
