package com.chefia.core.usecases.impl.usertype;

import com.chefia.core.usecases.interfaces.usertype.UserTypeInputPort;
import com.chefia.core.gateway.UserTypeGateway;
import com.chefia.core.gateway.UserTypeValidatorGateway;
import com.chefia.core.entities.UserType;
import com.chefia.core.exceptions.UserTypeNotFoundException;
import com.chefia.core.mapper.UserTypeMapper;
import com.chefia.usertypes.model.CreateUserTypeDTO;
import com.chefia.usertypes.model.PaginatedUserTypeDTO;
import com.chefia.usertypes.model.UpdateUserTypeDTO;
import com.chefia.usertypes.model.UserTypeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserTypeService implements UserTypeInputPort {

    private final UserTypeGateway userTypeGateway;
    private final List<UserTypeValidatorGateway> userTypeValidatorGateways;
    private final UserTypeMapper userTypeMapper;

    public UserTypeService(UserTypeGateway userTypeGateway,
                           List<UserTypeValidatorGateway> userTypeValidatorGateways,
                           UserTypeMapper userTypeMapper
    ) {
        this.userTypeGateway = userTypeGateway;
        this.userTypeValidatorGateways = userTypeValidatorGateways;
        this.userTypeMapper = userTypeMapper;
    }

    @Override
    public UserTypeDTO saveUserType(CreateUserTypeDTO createUserTypeDTO) {
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

    @Override
    public UserTypeDTO findById(Long userTypeId) {
        var userType = Optional.ofNullable(this.userTypeGateway
                .findById(userTypeId)
                .orElseThrow(() -> new UserTypeNotFoundException("User Type not found with id: " + userTypeId)));
        assert userType.isPresent();
        return this.userTypeMapper.toUserTypeResponseDTO(userType.get());
    }

    @Override
    public PaginatedUserTypeDTO findAll(Integer page, Integer perPage) {
        Pageable pageable = PageRequest.of(page, perPage);
        List<UserType> userTypePage = this.userTypeGateway.findAll(pageable);

        var userTypesDto = this.userTypeMapper.toResponseListDTO(userTypePage);

        return new PaginatedUserTypeDTO()
                .page(page)
                .perPage(perPage)
                .total((long) userTypePage.size())
                .items(userTypesDto);
    }

    @Override
    public UserTypeDTO updateUserType(Long userTypeId, UpdateUserTypeDTO updateUserTypeDTO) {
        var userTypeEntity = this.userTypeGateway
                .findById(userTypeId)
                .orElseThrow(() -> new UserTypeNotFoundException("User Type not found with id: " + userTypeId));

        userTypeEntity.setName(updateUserTypeDTO.getName());
        userTypeEntity.setDescription(updateUserTypeDTO.getDescription());
        userTypeEntity.setActive(updateUserTypeDTO.isActive());

        this.userTypeGateway.update(userTypeId, userTypeEntity);

        return this.userTypeMapper.toUserTypeResponseDTO(userTypeEntity);
    }

    @Override
    public void deleteUserType(Long userTypeId) {
        this.userTypeGateway.deleteById(userTypeId);
    }
}
