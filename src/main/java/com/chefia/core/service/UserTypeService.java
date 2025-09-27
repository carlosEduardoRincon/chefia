package com.chefia.core.service;

import com.chefia.core.port.input.UserTypeInputPort;
import com.chefia.core.port.output.UserTypeRepositoryOutputPort;
import com.chefia.domain.model.MenuItem;
import com.chefia.domain.model.UserType;
import com.chefia.infra.exception.MenuItemNotFoundException;
import com.chefia.infra.exception.UserTypeNotFoundException;
import com.chefia.infra.mapper.UserTypeMapper;
import com.chefia.menuitems.model.PaginatedMenuItemDTO;
import com.chefia.usertypes.model.CreateUserTypeDTO;
import com.chefia.usertypes.model.PaginatedUserTypeDTO;
import com.chefia.usertypes.model.UpdateUserTypeDTO;
import com.chefia.usertypes.model.UserTypeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserTypeService implements UserTypeInputPort {

    private final UserTypeRepositoryOutputPort userTypeRepositoryOutputPort;
    private final UserTypeMapper userTypeMapper;

    public UserTypeService(UserTypeRepositoryOutputPort userTypeRepositoryOutputPort, UserTypeMapper userTypeMapper) {
        this.userTypeRepositoryOutputPort = userTypeRepositoryOutputPort;
        this.userTypeMapper = userTypeMapper;
    }

    @Override
    public UserTypeDTO saveUserType(CreateUserTypeDTO createUserTypeDTO) {
        var userTypeToInsert = this.userTypeMapper.toEntity(createUserTypeDTO);
        this.userTypeRepositoryOutputPort.save(userTypeToInsert);

        return this.userTypeMapper.toUserTypeResponseDTO(userTypeToInsert);
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
        Page<UserType> userTypePage = this.userTypeRepositoryOutputPort.findAll(pageable);

        var userTypesDto = this.userTypeMapper.toResponseListDTO(userTypePage.getContent());

        return new PaginatedUserTypeDTO()
                .page(page)
                .perPage(perPage)
                .total(userTypePage.getTotalElements())
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
