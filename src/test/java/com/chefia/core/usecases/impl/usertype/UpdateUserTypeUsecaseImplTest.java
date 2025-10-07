package com.chefia.core.usecases.impl.usertype;

import com.chefia.core.entities.UserType;
import com.chefia.core.exceptions.UserTypeNotFoundException;
import com.chefia.core.gateway.UserTypeGateway;
import com.chefia.core.mapper.UserTypeMapper;
import com.chefia.usertypes.model.UpdateUserTypeDTO;
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
class UpdateUserTypeUsecaseImplTest {

    @Mock
    private UserTypeGateway userTypeGateway;

    @Mock
    private UserTypeMapper userTypeMapper;

    @InjectMocks
    private UpdateUserTypeUsecaseImpl updateUserTypeUsecase;

    private UserType existingUserType;
    private UpdateUserTypeDTO updateUserTypeDTO;
    private UserTypeDTO userTypeDTO;

    @BeforeEach
    void setUp() {
        existingUserType = new UserType();
        existingUserType.setNrSeqUserType(1L);
        existingUserType.setName("Old Admin");
        existingUserType.setDescription("Old admin description");
        existingUserType.setActive(true);

        updateUserTypeDTO = new UpdateUserTypeDTO();
        userTypeDTO = new UserTypeDTO();

        updateUserTypeUsecase = new UpdateUserTypeUsecaseImpl(userTypeGateway, userTypeMapper);
    }

    @Test
    void execute_ShouldReturnUpdatedUserTypeDTO_WhenUserTypeExists() {
        // Arrange
        var userTypeId = 1L;
        var newName = "Updated Admin";
        var newDescription = "Updated admin description";
        var newActive = false;

        updateUserTypeDTO.setName(newName);
        updateUserTypeDTO.setDescription(newDescription);
        updateUserTypeDTO.setActive(newActive);

        when(userTypeGateway.findById(anyLong())).thenReturn(Optional.of(existingUserType));
        when(userTypeMapper.toUserTypeResponseDTO(any(UserType.class))).thenReturn(userTypeDTO);

        // Act
        var result = updateUserTypeUsecase.execute(userTypeId, updateUserTypeDTO);

        // Assert
        assertNotNull(result);
        assertEquals(userTypeDTO, result);
        verify(userTypeGateway).findById(userTypeId);
        verify(userTypeGateway).update(userTypeId, existingUserType);
        verify(userTypeMapper).toUserTypeResponseDTO(existingUserType);

        // Verify that the entity was updated with new values
        assertEquals(newName, existingUserType.getName());
        assertEquals(newDescription, existingUserType.getDescription());
        assertEquals(newActive, existingUserType.getActive());
    }

    @Test
    void execute_ShouldThrowUserTypeNotFoundException_WhenUserTypeDoesNotExist() {
        // Arrange
        var userTypeId = 999L;
        when(userTypeGateway.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(UserTypeNotFoundException.class,
                    () -> updateUserTypeUsecase.execute(userTypeId, updateUserTypeDTO));

        assertEquals("User Type not found with id: " + userTypeId, exception.getMessage());
        verify(userTypeGateway).findById(userTypeId);
        verify(userTypeGateway, never()).update(anyLong(), any(UserType.class));
        verify(userTypeMapper, never()).toUserTypeResponseDTO(any(UserType.class));
    }

    @Test
    void execute_ShouldCallUpdateOnGateway_WhenUserTypeExists() {
        // Arrange
        var userTypeId = 1L;
        updateUserTypeDTO.setName("New Name");
        when(userTypeGateway.findById(anyLong())).thenReturn(Optional.of(existingUserType));
        when(userTypeMapper.toUserTypeResponseDTO(any(UserType.class))).thenReturn(userTypeDTO);

        // Act
        updateUserTypeUsecase.execute(userTypeId, updateUserTypeDTO);

        // Assert
        verify(userTypeGateway, times(1)).update(userTypeId, existingUserType);
    }

    @Test
    void execute_ShouldCallMethodsInCorrectOrder_WhenExecuted() {
        // Arrange
        var userTypeId = 1L;
        updateUserTypeDTO.setName("Test Name");
        when(userTypeGateway.findById(anyLong())).thenReturn(Optional.of(existingUserType));
        when(userTypeMapper.toUserTypeResponseDTO(any(UserType.class))).thenReturn(userTypeDTO);

        // Act
        updateUserTypeUsecase.execute(userTypeId, updateUserTypeDTO);

        // Assert
        var inOrder = inOrder(userTypeGateway, userTypeMapper);
        inOrder.verify(userTypeGateway).findById(userTypeId);
        inOrder.verify(userTypeGateway).update(userTypeId, existingUserType);
        inOrder.verify(userTypeMapper).toUserTypeResponseDTO(existingUserType);
    }

    @Test
    void execute_ShouldUpdateEntityFieldsDirectly_WhenExecuted() {
        // Arrange
        var userTypeId = 1L;
        var expectedName = "Direct Update Name";
        var expectedDescription = "Direct Update Description";
        var expectedActive = false;

        updateUserTypeDTO.setName(expectedName);
        updateUserTypeDTO.setDescription(expectedDescription);
        updateUserTypeDTO.setActive(expectedActive);

        when(userTypeGateway.findById(userTypeId)).thenReturn(Optional.of(existingUserType));
        when(userTypeMapper.toUserTypeResponseDTO(any(UserType.class))).thenReturn(userTypeDTO);

        // Act
        updateUserTypeUsecase.execute(userTypeId, updateUserTypeDTO);

        // Assert
        assertEquals(expectedName, existingUserType.getName());
        assertEquals(expectedDescription, existingUserType.getDescription());
        assertEquals(expectedActive, existingUserType.getActive());
    }

    @Test
    void execute_ShouldCallFindByIdWithCorrectParameter_WhenExecuted() {
        // Arrange
        var specificId = 7L;
        when(userTypeGateway.findById(specificId)).thenReturn(Optional.of(existingUserType));
        when(userTypeMapper.toUserTypeResponseDTO(any(UserType.class))).thenReturn(userTypeDTO);

        // Act
        updateUserTypeUsecase.execute(specificId, updateUserTypeDTO);

        // Assert
        verify(userTypeGateway).findById(specificId);
    }

    @Test
    void execute_ShouldUpdateNameCorrectly_WhenNameIsChanged() {
        // Arrange
        var userTypeId = 1L;
        var newName = "New UserType Name";
        updateUserTypeDTO.setName(newName);

        when(userTypeGateway.findById(userTypeId)).thenReturn(Optional.of(existingUserType));
        when(userTypeMapper.toUserTypeResponseDTO(existingUserType)).thenReturn(userTypeDTO);

        // Act
        updateUserTypeUsecase.execute(userTypeId, updateUserTypeDTO);

        // Assert
        assertEquals(newName, existingUserType.getName());
        verify(userTypeGateway).update(userTypeId, existingUserType);
        verify(userTypeMapper).toUserTypeResponseDTO(existingUserType);
    }

    @Test
    void execute_ShouldUpdateDescriptionCorrectly_WhenDescriptionIsChanged() {
        // Arrange
        var userTypeId = 1L;
        var newDescription = "New description for user type";
        updateUserTypeDTO.setDescription(newDescription);

        when(userTypeGateway.findById(userTypeId)).thenReturn(Optional.of(existingUserType));
        when(userTypeMapper.toUserTypeResponseDTO(existingUserType)).thenReturn(userTypeDTO);

        // Act
        updateUserTypeUsecase.execute(userTypeId, updateUserTypeDTO);

        // Assert
        assertEquals(newDescription, existingUserType.getDescription());
        verify(userTypeGateway).update(userTypeId, existingUserType);
        verify(userTypeMapper).toUserTypeResponseDTO(existingUserType);
    }

    @Test
    void execute_ShouldUpdateActiveStatusCorrectly_WhenActiveIsChanged() {
        // Arrange
        var userTypeId = 1L;
        var newActiveStatus = false;
        updateUserTypeDTO.setActive(newActiveStatus);

        when(userTypeGateway.findById(userTypeId)).thenReturn(Optional.of(existingUserType));
        when(userTypeMapper.toUserTypeResponseDTO(existingUserType)).thenReturn(userTypeDTO);

        // Act
        updateUserTypeUsecase.execute(userTypeId, updateUserTypeDTO);

        // Assert
        assertEquals(newActiveStatus, existingUserType.getActive());
        verify(userTypeGateway).update(userTypeId, existingUserType);
        verify(userTypeMapper).toUserTypeResponseDTO(existingUserType);
    }

    @Test
    void execute_ShouldHandleStatusChange_WhenUpdatingFromInactiveToActive() {
        // Arrange
        var userTypeId = 1L;
        var inactiveUserType = new UserType();
        inactiveUserType.setNrSeqUserType(userTypeId);
        inactiveUserType.setName("Manager");
        inactiveUserType.setDescription("Manager role");
        inactiveUserType.setActive(false);

        updateUserTypeDTO.setActive(true);

        when(userTypeGateway.findById(userTypeId)).thenReturn(Optional.of(inactiveUserType));
        when(userTypeMapper.toUserTypeResponseDTO(inactiveUserType)).thenReturn(userTypeDTO);

        // Act
        updateUserTypeUsecase.execute(userTypeId, updateUserTypeDTO);

        // Assert
        assertEquals(true, inactiveUserType.getActive());
        verify(userTypeGateway).update(userTypeId, inactiveUserType);
        verify(userTypeMapper).toUserTypeResponseDTO(inactiveUserType);
    }

    @Test
    void execute_ShouldPassCorrectEntityToMapper_WhenMappingResponse() {
        // Arrange
        var userTypeId = 3L;
        var specificUserType = new UserType();
        specificUserType.setNrSeqUserType(userTypeId);
        specificUserType.setName("Specific UserType");
        specificUserType.setDescription("Specific description");

        updateUserTypeDTO.setName("Updated Name");

        when(userTypeGateway.findById(userTypeId)).thenReturn(Optional.of(specificUserType));
        when(userTypeMapper.toUserTypeResponseDTO(specificUserType)).thenReturn(userTypeDTO);

        // Act
        updateUserTypeUsecase.execute(userTypeId, updateUserTypeDTO);

        // Assert
        verify(userTypeMapper).toUserTypeResponseDTO(specificUserType);
        assertEquals("Updated Name", specificUserType.getName());
    }

    @Test
    void execute_ShouldHandleNullValues_WhenDTOHasNullFields() {
        // Arrange
        var userTypeId = 1L;
        updateUserTypeDTO.setName(null);
        updateUserTypeDTO.setDescription(null);
        updateUserTypeDTO.setActive(false); // boolean can't be null, using false instead

        when(userTypeGateway.findById(userTypeId)).thenReturn(Optional.of(existingUserType));
        when(userTypeMapper.toUserTypeResponseDTO(any(UserType.class))).thenReturn(userTypeDTO);

        // Act
        updateUserTypeUsecase.execute(userTypeId, updateUserTypeDTO);

        // Assert
        assertNull(existingUserType.getName());
        assertNull(existingUserType.getDescription());
        assertEquals(false, existingUserType.getActive());
        verify(userTypeGateway).update(userTypeId, existingUserType);
    }
}