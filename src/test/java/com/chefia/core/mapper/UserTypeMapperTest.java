package com.chefia.core.mapper;

import com.chefia.core.entities.UserType;
import com.chefia.usertypes.model.CreateUserTypeDTO;
import com.chefia.usertypes.model.UserTypeDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserTypeMapperTest {

    @InjectMocks
    private UserTypeMapper userTypeMapper;

    private CreateUserTypeDTO createUserTypeDTO;
    private UserType userType;

    @BeforeEach
    void setUp() {
        createUserTypeDTO = new CreateUserTypeDTO();
        createUserTypeDTO.setName("Admin");
        createUserTypeDTO.setDescription("Administrator user type");

        userType = new UserType();
        userType.setNrSeqUserType(1L);
        userType.setName("Admin");
        userType.setDescription("Administrator user type");
        userType.setActive(true);
        userType.setCreatedAt(LocalDateTime.of(2023, 1, 1, 12, 0, 0));
    }

    @Test
    void toEntity_ShouldReturnUserType_WhenValidCreateUserTypeDTO() {
        // Act
        var result = userTypeMapper.toEntity(createUserTypeDTO);

        // Assert
        assertNotNull(result);
        assertEquals(createUserTypeDTO.getName(), result.getName());
        assertEquals(createUserTypeDTO.getDescription(), result.getDescription());
        assertTrue(result.getActive());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    void toEntity_ShouldSetActiveToTrue_WhenCalled() {
        // Act
        var result = userTypeMapper.toEntity(createUserTypeDTO);

        // Assert
        assertTrue(result.getActive());
    }

    @Test
    void toEntity_ShouldSetCreatedAtToCurrentTime_WhenCalled() {
        // Arrange
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);

        // Act
        var result = userTypeMapper.toEntity(createUserTypeDTO);

        // Assert
        LocalDateTime after = LocalDateTime.now().plusSeconds(1);
        assertNotNull(result.getCreatedAt());
        assertTrue(result.getCreatedAt().isAfter(before));
        assertTrue(result.getCreatedAt().isBefore(after));
    }

    @Test
    void toEntity_ShouldMapNameAndDescription_WhenCalled() {
        // Arrange
        createUserTypeDTO.setName("Manager");
        createUserTypeDTO.setDescription("Manager user type");

        // Act
        var result = userTypeMapper.toEntity(createUserTypeDTO);

        // Assert
        assertEquals("Manager", result.getName());
        assertEquals("Manager user type", result.getDescription());
    }

    @Test
    void toUserTypeResponseDTO_ShouldReturnUserTypeDTO_WhenValidUserType() {
        // Act
        var result = userTypeMapper.toUserTypeResponseDTO(userType);

        // Assert
        assertNotNull(result);
        assertEquals(userType.getNrSeqUserType(), result.getId());
        assertEquals(userType.getName(), result.getName());
        assertEquals(userType.getDescription(), result.getDescription());
        assertEquals(userType.getActive(), result.isActive());
    }

    @Test
    void toUserTypeResponseDTO_ShouldMapDatesWithTimezone_WhenCalled() {
        // Arrange
        userType.setUpdatedAt(LocalDateTime.of(2023, 1, 2, 12, 0, 0));

        // Act
        var result = userTypeMapper.toUserTypeResponseDTO(userType);

        // Assert
        OffsetDateTime expectedCreatedAt = userType.getCreatedAt().atOffset(ZoneOffset.ofHours(-3));
        OffsetDateTime expectedUpdatedAt = userType.getUpdatedAt().atOffset(ZoneOffset.ofHours(-3));

        assertEquals(expectedCreatedAt, result.getCreatedAt());
        assertEquals(expectedUpdatedAt, result.getUpdatedAt());
    }

    @Test
    void toUserTypeResponseDTO_ShouldMapAllFields_WhenCalled() {
        // Act
        var result = userTypeMapper.toUserTypeResponseDTO(userType);

        // Assert
        assertEquals(1L, result.getId());
        assertEquals("Admin", result.getName());
        assertEquals("Administrator user type", result.getDescription());
        assertTrue(result.isActive());
    }

    @Test
    void toResponseListDTO_ShouldReturnEmptyList_WhenEmptyUserTypeList() {
        // Arrange
        List<UserType> emptyList = new ArrayList<>();

        // Act
        var result = userTypeMapper.toResponseListDTO(emptyList);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toResponseListDTO_ShouldReturnListOfUserTypeDTO_WhenValidUserTypeList() {
        // Arrange
        UserType userType2 = new UserType();
        userType2.setNrSeqUserType(2L);
        userType2.setName("User");
        userType2.setDescription("Regular user type");
        userType2.setActive(false);
        userType2.setCreatedAt(LocalDateTime.now());

        List<UserType> userTypeList = Arrays.asList(userType, userType2);

        // Act
        var result = userTypeMapper.toResponseListDTO(userTypeList);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(userType.getNrSeqUserType(), result.get(0).getId());
        assertEquals(userType2.getNrSeqUserType(), result.get(1).getId());
        assertEquals(userType.getName(), result.get(0).getName());
        assertEquals(userType2.getName(), result.get(1).getName());
    }

    @Test
    void toResponseListDTO_ShouldMapAllUserTypes_WhenCalled() {
        // Arrange
        UserType userType1 = new UserType();
        userType1.setNrSeqUserType(1L);
        userType1.setName("Admin");
        userType1.setDescription("Admin description");
        userType1.setActive(true);
        userType1.setCreatedAt(LocalDateTime.now());

        UserType userType2 = new UserType();
        userType2.setNrSeqUserType(2L);
        userType2.setName("User");
        userType2.setDescription("User description");
        userType2.setActive(false);
        userType2.setCreatedAt(LocalDateTime.now());

        List<UserType> userTypeList = Arrays.asList(userType1, userType2);

        // Act
        var result = userTypeMapper.toResponseListDTO(userTypeList);

        // Assert
        assertEquals(2, result.size());

        UserTypeDTO firstResult = result.get(0);
        assertEquals(1L, firstResult.getId());
        assertEquals("Admin", firstResult.getName());
        assertEquals("Admin description", firstResult.getDescription());
        assertTrue(firstResult.isActive());

        UserTypeDTO secondResult = result.get(1);
        assertEquals(2L, secondResult.getId());
        assertEquals("User", secondResult.getName());
        assertEquals("User description", secondResult.getDescription());
        assertFalse(secondResult.isActive());
    }

    @Test
    void toResponseListDTO_ShouldHandleNullUpdatedAtInList_WhenSomeItemsHaveNullUpdatedAt() {
        // Arrange
        UserType userTypeWithoutUpdate = new UserType();
        userTypeWithoutUpdate.setNrSeqUserType(3L);
        userTypeWithoutUpdate.setName("Guest");
        userTypeWithoutUpdate.setDescription("Guest user type");
        userTypeWithoutUpdate.setActive(true);
        userTypeWithoutUpdate.setCreatedAt(LocalDateTime.now());

        List<UserType> userTypeList = Arrays.asList(userType, userTypeWithoutUpdate);

        // Act
        var result = userTypeMapper.toResponseListDTO(userTypeList);

        // Assert
        assertEquals(2, result.size());
        assertNull(result.get(0).getUpdatedAt());
    }

    @Test
    void toEntity_ShouldWorkWithDifferentData_WhenCalled() {
        // Arrange
        createUserTypeDTO.setName("Customer");
        createUserTypeDTO.setDescription("Customer user type with longer description");

        // Act
        var result = userTypeMapper.toEntity(createUserTypeDTO);

        // Assert
        assertEquals("Customer", result.getName());
        assertEquals("Customer user type with longer description", result.getDescription());
        assertTrue(result.getActive());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    void toUserTypeResponseDTO_ShouldWorkWithDifferentActiveStates_WhenCalled() {
        // Arrange
        userType.setActive(false);

        // Act
        var result = userTypeMapper.toUserTypeResponseDTO(userType);

        // Assert
        assertFalse(result.isActive());
        assertEquals(userType.getName(), result.getName());
    }

    @Test
    void toUserTypeResponseDTO_ShouldHandleNullUpdatedAt_WhenUpdatedAtIsNull() {
        // Act
        var result = userTypeMapper.toUserTypeResponseDTO(userType);

        // Assert
        assertNull(result.getUpdatedAt());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    void toUserTypeResponseDTO_ShouldApplyCorrectTimezone_WhenCalled() {
        // Arrange
        userType.setCreatedAt(LocalDateTime.of(2023, 6, 15, 14, 30, 45));
        userType.setUpdatedAt(LocalDateTime.of(2023, 6, 16, 10, 15, 20));

        // Act
        var result = userTypeMapper.toUserTypeResponseDTO(userType);

        // Assert
        assertEquals(ZoneOffset.ofHours(-3), result.getCreatedAt().getOffset());
        assertEquals(ZoneOffset.ofHours(-3), result.getUpdatedAt().getOffset());
    }
}
