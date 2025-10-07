package com.chefia.core.usecases.impl.user;

import com.chefia.core.entities.User;
import com.chefia.core.exceptions.UserNotFoundException;
import com.chefia.core.gateway.UserGateway;
import com.chefia.core.mapper.UserMapper;
import com.chefia.core.usecases.interfaces.user.ReadUserUsecase;
import com.chefia.users.model.UserDTO;
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
class ReadUserUsecaseImplTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private ReadUserUsecaseImpl readUserUsecase;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setNrSeqUser(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setActive(true);

        userDTO = new UserDTO();

        readUserUsecase = new ReadUserUsecaseImpl(userGateway, userMapper);
    }

    @Test
    void execute_ShouldReturnUserDTO_WhenUserExists() {
        // Arrange
        var userId = 1L;
        when(userGateway.findById(anyLong())).thenReturn(Optional.of(user));
        when(userMapper.toUserResponseDTO(any(User.class))).thenReturn(userDTO);

        // Act
        var result = readUserUsecase.execute(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userDTO, result);
        verify(userGateway).findById(userId);
        verify(userMapper).toUserResponseDTO(user);
    }

    @Test
    void execute_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        // Arrange
        var userId = 999L;
        when(userGateway.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, 
                    () -> readUserUsecase.execute(userId));

        verify(userGateway).findById(userId);
        verify(userMapper, never()).toUserResponseDTO(any(User.class));
    }

    @Test
    void execute_ShouldCallGatewayWithCorrectId_WhenExecuted() {
        // Arrange
        var specificId = 5L;
        when(userGateway.findById(specificId)).thenReturn(Optional.of(user));
        when(userMapper.toUserResponseDTO(any(User.class))).thenReturn(userDTO);

        // Act
        readUserUsecase.execute(specificId);

        // Assert
        verify(userGateway).findById(specificId);
    }

    @Test
    void execute_ShouldCallMapperOnce_WhenUserExists() {
        // Arrange
        var userId = 1L;
        when(userGateway.findById(anyLong())).thenReturn(Optional.of(user));
        when(userMapper.toUserResponseDTO(any(User.class))).thenReturn(userDTO);

        // Act
        readUserUsecase.execute(userId);

        // Assert
        verify(userMapper, times(1)).toUserResponseDTO(user);
    }

    @Test
    void execute_ShouldPassCorrectEntityToMapper_WhenMappingResponse() {
        // Arrange
        var userId = 1L;
        var specificUser = new User();
        specificUser.setNrSeqUser(userId);
        specificUser.setName("Jane Smith");
        specificUser.setEmail("jane@example.com");

        when(userGateway.findById(userId)).thenReturn(Optional.of(specificUser));
        when(userMapper.toUserResponseDTO(specificUser)).thenReturn(userDTO);

        // Act
        readUserUsecase.execute(userId);

        // Assert
        verify(userMapper).toUserResponseDTO(specificUser);
    }
}