package com.chefia.infra.mapper;

import com.chefia.domain.model.UserType;
import com.chefia.users.model.UserDTO;
import com.chefia.usertypes.model.CreateUserTypeDTO;
import com.chefia.usertypes.model.UserTypeDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserTypeMapper {
    public UserType toEntity(CreateUserTypeDTO createUserTypeDTO) {
        return new UserType(createUserTypeDTO.getName(),
                createUserTypeDTO.getDescription(),
                true,
                LocalDateTime.now()
                );
    }

    public UserTypeDTO toUserTypeResponseDTO(UserType menuItemToInsert) {
        return new UserTypeDTO()
                .id(menuItemToInsert.getNrSeqUserType())
                .name(menuItemToInsert.getName())
                .description(menuItemToInsert.getDescription())
                .active(menuItemToInsert.getActive())
                .createdAt(menuItemToInsert.getCreatedAt().atOffset(ZoneOffset.ofHours(-3)))
                .updatedAt(menuItemToInsert.getUpdatedAt() != null? menuItemToInsert.getUpdatedAt().atOffset(ZoneOffset.ofHours(-3)): null);
    }

    public List<UserTypeDTO> toResponseListDTO(List<UserType> userTypeList) {
        var userTypesResponse = new ArrayList<UserTypeDTO>();
        for (var userType : userTypeList) {
            userTypesResponse.add(this.toUserTypeResponseDTO(userType));
        }
        return userTypesResponse;
    }
}
