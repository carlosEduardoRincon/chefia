package com.chefia.core.usecases.impl.usertype;

import com.chefia.core.entities.UserType;
import com.chefia.core.gateway.UserTypeGateway;
import com.chefia.core.gateway.UserTypeValidatorGateway;
import com.chefia.core.mapper.UserTypeMapper;
import com.chefia.usertypes.model.CreateUserTypeDTO;
import com.chefia.usertypes.model.UserTypeDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserTypeUsecaseImplTest {

    @Mock
    private UserTypeGateway userTypeGateway;

    @Mock
    private List<UserTypeValidatorGateway> userTypeValidatorGateways;

    @Mock
    private UserTypeMapper userTypeMapper;

    @Mock
    private UserTypeValidatorGateway validator1;

    @Mock
    private UserTypeValidatorGateway validator2;

    @InjectMocks
    private CreateUserTypeUsecaseImpl createUserTypeUsecase;

    private CreateUserTypeDTO createUserTypeDTO;
    private UserType userType;
    private UserTypeDTO userTypeDTO;

    @BeforeEach
    void setUp() {
        createUserTypeDTO = new CreateUserTypeDTO();
        userType = new UserType();
        userType.setNrSeqUserType(1L);
        userType.setName("Admin");

        userTypeDTO = new UserTypeDTO();

        userTypeValidatorGateways = Arrays.asList(validator1, validator2);
        createUserTypeUsecase = new CreateUserTypeUsecaseImpl(userTypeGateway, userTypeValidatorGateways, userTypeMapper);
    }

    @Test
    void execute_ShouldReturnUserTypeDTO_WhenValidData() {
        // Arrange
        var savedId = 1L;
        when(userTypeMapper.toEntity(any(CreateUserTypeDTO.class))).thenReturn(userType);
        when(userTypeGateway.save(any(UserType.class))).thenReturn(savedId);
        when(userTypeMapper.toUserTypeResponseDTO(any(UserType.class))).thenReturn(userTypeDTO);

        // Act
        var result = createUserTypeUsecase.execute(createUserTypeDTO);

        // Assert
        assertNotNull(result);
        assertEquals(userTypeDTO, result);
        verify(userTypeMapper).toEntity(createUserTypeDTO);
        verify(userTypeGateway).save(userType);
        verify(userTypeMapper).toUserTypeResponseDTO(userType);
    }

    @Test
    void execute_ShouldCallAllValidators_WhenExecuted() {
        // Arrange
        var savedId = 1L;
        when(userTypeMapper.toEntity(any(CreateUserTypeDTO.class))).thenReturn(userType);
        when(userTypeGateway.save(any(UserType.class))).thenReturn(savedId);
        when(userTypeMapper.toUserTypeResponseDTO(any(UserType.class))).thenReturn(userTypeDTO);

        // Act
        createUserTypeUsecase.execute(createUserTypeDTO);

        // Assert
        verify(validator1).validate(userType);
        verify(validator2).validate(userType);
    }

    @Test
    void execute_ShouldSetUserTypeId_WhenSaved() {
        // Arrange
        var savedId = 2L;
        var userTypeToVerify = new UserType();
        userTypeToVerify.setName("Admin");

        when(userTypeMapper.toEntity(any(CreateUserTypeDTO.class))).thenReturn(userTypeToVerify);
        when(userTypeGateway.save(any(UserType.class))).thenReturn(savedId);
        when(userTypeMapper.toUserTypeResponseDTO(any(UserType.class))).thenReturn(userTypeDTO);

        // Act
        createUserTypeUsecase.execute(createUserTypeDTO);

        // Assert
        assertEquals(savedId, userTypeToVerify.getNrSeqUserType());
        verify(userTypeMapper).toUserTypeResponseDTO(userTypeToVerify);
    }

    @Test
    void execute_ShouldCallMethodsInCorrectOrder_WhenExecuted() {
        // Arrange
        var savedId = 1L;
        when(userTypeMapper.toEntity(any(CreateUserTypeDTO.class))).thenReturn(userType);
        when(userTypeGateway.save(any(UserType.class))).thenReturn(savedId);
        when(userTypeMapper.toUserTypeResponseDTO(any(UserType.class))).thenReturn(userTypeDTO);

        // Act
        createUserTypeUsecase.execute(createUserTypeDTO);

        // Assert
        var inOrder = inOrder(userTypeMapper, validator1, validator2, userTypeGateway);
        inOrder.verify(userTypeMapper).toEntity(createUserTypeDTO);
        inOrder.verify(validator1).validate(userType);
        inOrder.verify(validator2).validate(userType);
        inOrder.verify(userTypeGateway).save(userType);
        inOrder.verify(userTypeMapper).toUserTypeResponseDTO(userType);
    }
}
