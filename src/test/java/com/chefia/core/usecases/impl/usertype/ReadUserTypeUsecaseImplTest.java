package com.chefia.core.usecases.impl.usertype;

import com.chefia.core.entities.UserType;
import com.chefia.core.exceptions.UserTypeNotFoundException;
import com.chefia.core.gateway.UserTypeGateway;
import com.chefia.core.mapper.UserTypeMapper;
import com.chefia.usertypes.model.UserTypeDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadUserTypeUsecaseImplTest {

    @Mock
    private UserTypeGateway userTypeGateway;

    @Mock
    private UserTypeMapper userTypeMapper;

    @InjectMocks
    private ReadUserTypeUsecaseImpl readUserTypeUsecase;

    private UserType userType;
    private UserTypeDTO userTypeDTO;

    @BeforeEach
    void setUp() {
        userType = new UserType();
        userType.setNrSeqUserType(1L);
        userType.setName("Administrator");
        userType.setDescription("System administrator with full access");
        userType.setActive(true);

        userTypeDTO = new UserTypeDTO();
    }

    @Test
    void execute_ShouldReturnUserTypeDTO_WhenUserTypeExists() {
        // Arrange
        var userTypeId = 1L;
        when(userTypeGateway.findById(anyLong())).thenReturn(Optional.of(userType));
        when(userTypeMapper.toUserTypeResponseDTO(any(UserType.class))).thenReturn(userTypeDTO);

        // Act
        var result = readUserTypeUsecase.execute(userTypeId);

        // Assert
        assertNotNull(result);
        assertEquals(userTypeDTO, result);
        verify(userTypeGateway).findById(userTypeId);
        verify(userTypeMapper).toUserTypeResponseDTO(userType);
    }

    @Test
    void execute_ShouldThrowUserTypeNotFoundException_WhenUserTypeDoesNotExist() {
        // Arrange
        var userTypeId = 999L;
        when(userTypeGateway.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserTypeNotFoundException.class, 
                    () -> readUserTypeUsecase.execute(userTypeId));

        verify(userTypeGateway).findById(userTypeId);
        verify(userTypeMapper, never()).toUserTypeResponseDTO(any(UserType.class));
    }

    @Test
    void execute_ShouldCallGatewayWithCorrectId_WhenExecuted() {
        // Arrange
        var specificId = 5L;
        when(userTypeGateway.findById(specificId)).thenReturn(Optional.of(userType));
        when(userTypeMapper.toUserTypeResponseDTO(any(UserType.class))).thenReturn(userTypeDTO);

        // Act
        readUserTypeUsecase.execute(specificId);

        // Assert
        verify(userTypeGateway).findById(specificId);
    }

    @Test
    void execute_ShouldCallMapperOnce_WhenUserTypeExists() {
        // Arrange
        var userTypeId = 1L;
        when(userTypeGateway.findById(anyLong())).thenReturn(Optional.of(userType));
        when(userTypeMapper.toUserTypeResponseDTO(any(UserType.class))).thenReturn(userTypeDTO);

        // Act
        readUserTypeUsecase.execute(userTypeId);

        // Assert
        verify(userTypeMapper, times(1)).toUserTypeResponseDTO(userType);
    }

    @Test
    void execute_ShouldPassCorrectEntityToMapper_WhenMappingResponse() {
        // Arrange
        var userTypeId = 1L;
        var specificUserType = new UserType();
        specificUserType.setNrSeqUserType(userTypeId);
        specificUserType.setName("Manager");
        specificUserType.setDescription("Restaurant manager role");
        specificUserType.setActive(false);

        when(userTypeGateway.findById(userTypeId)).thenReturn(Optional.of(specificUserType));
        when(userTypeMapper.toUserTypeResponseDTO(specificUserType)).thenReturn(userTypeDTO);

        // Act
        readUserTypeUsecase.execute(userTypeId);

        // Assert
        verify(userTypeMapper).toUserTypeResponseDTO(specificUserType);
    }

    @Test
    void execute_ShouldHandleInactiveUserType_WhenUserTypeExists() {
        // Arrange
        var userTypeId = 2L;
        var inactiveUserType = new UserType();
        inactiveUserType.setNrSeqUserType(userTypeId);
        inactiveUserType.setName("Chef");
        inactiveUserType.setDescription("Head chef role");
        inactiveUserType.setActive(false);

        when(userTypeGateway.findById(userTypeId)).thenReturn(Optional.of(inactiveUserType));
        when(userTypeMapper.toUserTypeResponseDTO(inactiveUserType)).thenReturn(userTypeDTO);

        // Act
        var result = readUserTypeUsecase.execute(userTypeId);

        // Assert
        assertNotNull(result);
        assertEquals(userTypeDTO, result);
        verify(userTypeGateway).findById(userTypeId);
        verify(userTypeMapper).toUserTypeResponseDTO(inactiveUserType);
    }
}
