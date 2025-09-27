package com.chefia.infra.mapper;

import com.chefia.domain.model.UserType;
import com.chefia.usertypes.model.CreateUserTypeDTO;
import com.chefia.usertypes.model.UserTypeDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserTypeMapper {
    public UserType toEntity(CreateUserTypeDTO createUserTypeDTO) {
        return new UserType();
    }

    public UserTypeDTO toUserTypeResponseDTO(UserType menuItemToInsert) {
        return new UserTypeDTO();
    }

    public List<UserTypeDTO> toResponseListDTO(List<UserType> content) {
        return new ArrayList<>();
    }
}
