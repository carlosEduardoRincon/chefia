package com.chefia.core.usecases.impl.usertype;

import com.chefia.core.exceptions.UserTypeNotFoundException;
import com.chefia.core.gateway.UserTypeGateway;
import com.chefia.core.mapper.UserTypeMapper;
import com.chefia.core.usecases.interfaces.usertype.UpdateUserTypeUsecase;
import com.chefia.usertypes.model.UpdateUserTypeDTO;
import com.chefia.usertypes.model.UserTypeDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UpdateUserTypeUsecaseImpl implements UpdateUserTypeUsecase {

    private final UserTypeGateway userTypeGateway;
    private final UserTypeMapper userTypeMapper;

    @Override
    public UserTypeDTO execute(Long userTypeId, UpdateUserTypeDTO updateUserTypeDTO) {
        var userTypeEntity = this.userTypeGateway
                .findById(userTypeId)
                .orElseThrow(() -> new UserTypeNotFoundException("User Type not found with id: " + userTypeId));

        userTypeEntity.setName(updateUserTypeDTO.getName());
        userTypeEntity.setDescription(updateUserTypeDTO.getDescription());
        userTypeEntity.setActive(updateUserTypeDTO.isActive());

        this.userTypeGateway.update(userTypeId, userTypeEntity);

        return this.userTypeMapper.toUserTypeResponseDTO(userTypeEntity);
    }
}
