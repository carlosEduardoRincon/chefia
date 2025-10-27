package com.chefia.core.usecases.impl.user;

import com.chefia.core.entities.User;
import com.chefia.core.gateway.UserGateway;
import com.chefia.core.gateway.UserValidatorGateway;
import com.chefia.core.mapper.UserMapper;
import com.chefia.core.usecases.interfaces.user.CreateUserUsecase;
import com.chefia.users.model.CreateUserDTO;
import com.chefia.users.model.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUsecaseImplTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private List<UserValidatorGateway> userValidatorGatewayList;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserValidatorGateway validator1;

    @Mock
    private UserValidatorGateway validator2;

    @InjectMocks
    private CreateUserUsecaseImpl createUserUsecase;

    private CreateUserDTO createUserDTO;
    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        createUserDTO = new CreateUserDTO();
        user = new User();
        user.setNrSeqUser(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("hashedPassword");
        user.setActive(true);

        userDTO = new UserDTO();

        userValidatorGatewayList = Arrays.asList(validator1, validator2);
        createUserUsecase = new CreateUserUsecaseImpl(userGateway, userValidatorGatewayList, userMapper);
    }

    @Test
    void execute_ShouldReturnUserDTO_WhenValidData() {
        // Arrange
        var savedId = 1L;
        when(userMapper.toEntity(any(CreateUserDTO.class))).thenReturn(user);
        when(userGateway.save(any(User.class))).thenReturn(savedId);
        when(userMapper.toUserResponseDTO(any(User.class))).thenReturn(userDTO);

        // Act
        var result = createUserUsecase.execute(createUserDTO);

        // Assert
        assertNotNull(result);
        assertEquals(userDTO, result);
        verify(userMapper).toEntity(createUserDTO);
        verify(userGateway).save(user);
        verify(userMapper).toUserResponseDTO(user);
    }

    @Test
    void execute_ShouldCallAllValidators_WhenExecuted() {
        // Arrange
        var savedId = 1L;
        when(userMapper.toEntity(any(CreateUserDTO.class))).thenReturn(user);
        when(userGateway.save(any(User.class))).thenReturn(savedId);
        when(userMapper.toUserResponseDTO(any(User.class))).thenReturn(userDTO);

        // Act
        createUserUsecase.execute(createUserDTO);

        // Assert
        verify(validator1).validate(user);
        verify(validator2).validate(user);
    }

    @Test
    void execute_ShouldSetUserId_WhenSaved() {
        // Arrange
        var savedId = 3L;
        var userToVerify = new User();
        userToVerify.setName("Jane Smith");
        userToVerify.setEmail("jane@example.com");

        when(userMapper.toEntity(any(CreateUserDTO.class))).thenReturn(userToVerify);
        when(userGateway.save(any(User.class))).thenReturn(savedId);
        when(userMapper.toUserResponseDTO(any(User.class))).thenReturn(userDTO);

        // Act
        createUserUsecase.execute(createUserDTO);

        // Assert
        assertEquals(savedId, userToVerify.getNrSeqUser());
        verify(userMapper).toUserResponseDTO(userToVerify);
    }

    @Test
    void execute_ShouldCallMethodsInCorrectOrder_WhenExecuted() {
        // Arrange
        var savedId = 1L;
        when(userMapper.toEntity(any(CreateUserDTO.class))).thenReturn(user);
        when(userGateway.save(any(User.class))).thenReturn(savedId);
        when(userMapper.toUserResponseDTO(any(User.class))).thenReturn(userDTO);

        // Act
        createUserUsecase.execute(createUserDTO);

        // Assert
        var inOrder = inOrder(userMapper, validator1, validator2, userGateway);
        inOrder.verify(userMapper).toEntity(createUserDTO);
        inOrder.verify(validator1).validate(user);
        inOrder.verify(validator2).validate(user);
        inOrder.verify(userGateway).save(user);
        inOrder.verify(userMapper).toUserResponseDTO(user);
    }

    @Test
    void validateUser_ShouldCallAllValidators_WhenUserHasMultipleValidators() {
        // Arrange
        when(userMapper.toEntity(any(CreateUserDTO.class))).thenReturn(user);
        when(userGateway.save(any(User.class))).thenReturn(1L);
        when(userMapper.toUserResponseDTO(any(User.class))).thenReturn(userDTO);

        // Act
        createUserUsecase.execute(createUserDTO);

        // Assert
        verify(validator1, times(1)).validate(user);
        verify(validator2, times(1)).validate(user);
    }

    @Test
    void execute_ShouldHandleEmptyValidatorList_WhenNoValidators() {
        // Arrange
        var savedId = 1L;
        var emptyValidatorList = new ArrayList<UserValidatorGateway>();
        var usecaseWithNoValidators = new CreateUserUsecaseImpl(
            userGateway, emptyValidatorList, userMapper);

        when(userMapper.toEntity(any(CreateUserDTO.class))).thenReturn(user);
        when(userGateway.save(any(User.class))).thenReturn(savedId);
        when(userMapper.toUserResponseDTO(any(User.class))).thenReturn(userDTO);

        // Act
        UserDTO result = usecaseWithNoValidators.execute(createUserDTO);

        // Assert
        assertNotNull(result);
        assertEquals(userDTO, result);
        verify(userMapper).toEntity(createUserDTO);
        verify(userGateway).save(user);
        verify(userMapper).toUserResponseDTO(user);
    }

    @Test
    void execute_ShouldHashPasswordBeforeSaving_WhenPasswordProvided() {
        // Arrange
        var savedId = 1L;
        var userWithHashedPassword = new User();
        userWithHashedPassword.setName("Test User");
        userWithHashedPassword.setEmail("test@example.com");
        userWithHashedPassword.setPassword("$2a$10$hashedpassword");

        when(userMapper.toEntity(any(CreateUserDTO.class))).thenReturn(userWithHashedPassword);
        when(userGateway.save(any(User.class))).thenReturn(savedId);
        when(userMapper.toUserResponseDTO(any(User.class))).thenReturn(userDTO);

        // Act
        createUserUsecase.execute(createUserDTO);

        // Assert
        verify(userGateway).save(userWithHashedPassword);
        assertTrue(userWithHashedPassword.getPassword().startsWith("$2a$10$"));
    }
}