package com.chefia.core.service;

import com.chefia.core.port.input.usertype.UserTypeInputPort;
import com.chefia.core.port.output.usertype.UserTypeRepositoryOutputPort;
import com.chefia.core.port.output.usertype.UserTypeValidatorOutputPort;
import com.chefia.domain.model.UserType;
import com.chefia.infra.exception.UserTypeNotFoundException;
import com.chefia.infra.mapper.UserTypeMapper;
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

    private final UserTypeRepositoryOutputPort userTypeRepositoryOutputPort;
    private final List<UserTypeValidatorOutputPort> userTypeValidatorOutputPorts;
    private final UserTypeMapper userTypeMapper;

    public UserTypeService(UserTypeRepositoryOutputPort userTypeRepositoryOutputPort,
                           List<UserTypeValidatorOutputPort> userTypeValidatorOutputPorts,
                           UserTypeMapper userTypeMapper
    ) {
        this.userTypeRepositoryOutputPort = userTypeRepositoryOutputPort;
        this.userTypeValidatorOutputPorts = userTypeValidatorOutputPorts;
        this.userTypeMapper = userTypeMapper;
    }

    @Override
    public UserTypeDTO saveUserType(CreateUserTypeDTO createUserTypeDTO) {
        var userTypeToInsert = this.userTypeMapper.toEntity(createUserTypeDTO);
        this.validateUserType(userTypeToInsert);

        var userTypeId = this.userTypeRepositoryOutputPort.save(userTypeToInsert);

        userTypeToInsert.setNrSeqUserType(userTypeId);

        return this.userTypeMapper.toUserTypeResponseDTO(userTypeToInsert);
    }

    private void validateUserType(UserType userType) {
        for (UserTypeValidatorOutputPort userValidator : userTypeValidatorOutputPorts) {
            userValidator.validate(userType);
        }
    }

    @Override
    public UserTypeDTO findById(Long userTypeId) {
        var userType = Optional.ofNullable(this.userTypeRepositoryOutputPort
                .findById(userTypeId)
                .orElseThrow(() -> new UserTypeNotFoundException("User Type not found with id: " + userTypeId)));
        assert userType.isPresent();
        return this.userTypeMapper.toUserTypeResponseDTO(userType.get());
    }

    @Override
    public PaginatedUserTypeDTO findAll(Integer page, Integer perPage) {
        Pageable pageable = PageRequest.of(page, perPage);
        List<UserType> userTypePage = this.userTypeRepositoryOutputPort.findAll(pageable);

        var userTypesDto = this.userTypeMapper.toResponseListDTO(userTypePage);

        return new PaginatedUserTypeDTO()
                .page(page)
                .perPage(perPage)
                .total((long) userTypePage.size())
                .items(userTypesDto);
    }

    @Override
    public UserTypeDTO updateUserType(Long userTypeId, UpdateUserTypeDTO updateUserTypeDTO) {
        var userTypeEntity = this.userTypeRepositoryOutputPort
                .findById(userTypeId)
                .orElseThrow(() -> new UserTypeNotFoundException("User Type not found with id: " + userTypeId));

        userTypeEntity.setName(updateUserTypeDTO.getName());
        userTypeEntity.setDescription(updateUserTypeDTO.getDescription());
        userTypeEntity.setActive(updateUserTypeDTO.isActive());

        this.userTypeRepositoryOutputPort.update(userTypeId, userTypeEntity);

        return this.userTypeMapper.toUserTypeResponseDTO(userTypeEntity);
    }

    @Override
    public void deleteUserType(Long userTypeId) {
        this.userTypeRepositoryOutputPort.deleteById(userTypeId);
    }
}
