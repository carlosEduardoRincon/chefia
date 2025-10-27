package com.chefia.core.usecases.impl.usertype;

import com.chefia.core.entities.UserType;
import com.chefia.core.gateway.UserTypeGateway;
import com.chefia.core.mapper.UserTypeMapper;
import com.chefia.usertypes.model.PaginatedUserTypeDTO;
import com.chefia.usertypes.model.UserTypeDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadAllUserTypeUsecaseImplTest {

    @Mock
    private UserTypeGateway userTypeGateway;

    @Mock
    private UserTypeMapper userTypeMapper;

    @InjectMocks
    private ReadAllUserTypeUsecaseImpl readAllUserTypeUsecase;

    private List<UserType> userTypeList;
    private List<UserTypeDTO> userTypeDTOList;

    @BeforeEach
    void setUp() {
        var userType1 = new UserType();
        userType1.setNrSeqUserType(1L);
        userType1.setName("Administrator");
        userType1.setDescription("System administrator with full access");
        userType1.setActive(true);

        var userType2 = new UserType();
        userType2.setNrSeqUserType(2L);
        userType2.setName("Manager");
        userType2.setDescription("Restaurant manager");
        userType2.setActive(true);

        userTypeList = Arrays.asList(userType1, userType2);

        var userTypeDTO1 = new UserTypeDTO();
        var userTypeDTO2 = new UserTypeDTO();
        userTypeDTOList = Arrays.asList(userTypeDTO1, userTypeDTO2);

        readAllUserTypeUsecase = new ReadAllUserTypeUsecaseImpl(userTypeGateway, userTypeMapper);
    }

    @Test
    void execute_ShouldReturnPaginatedUserTypeDTO_WhenUserTypesExist() {
        // Arrange
        var page = 0;
        var perPage = 10;
        when(userTypeGateway.findAll(any(Pageable.class))).thenReturn(userTypeList);
        when(userTypeMapper.toResponseListDTO(anyList())).thenReturn(userTypeDTOList);

        // Act
        var result = readAllUserTypeUsecase.execute(page, perPage);

        // Assert
        assertNotNull(result);
        assertEquals(page, result.getPage());
        assertEquals(perPage, result.getPerPage());
        assertEquals(Long.valueOf(userTypeList.size()), result.getTotal());
        assertEquals(userTypeDTOList, result.getItems());
        verify(userTypeGateway).findAll(any(Pageable.class));
        verify(userTypeMapper).toResponseListDTO(userTypeList);
    }

    @Test
    void execute_ShouldCallGatewayWithCorrectPageable_WhenExecuted() {
        // Arrange
        var page = 2;
        var perPage = 5;
        var expectedPageable = PageRequest.of(page, perPage);
        when(userTypeGateway.findAll(any(Pageable.class))).thenReturn(userTypeList);
        when(userTypeMapper.toResponseListDTO(anyList())).thenReturn(userTypeDTOList);

        // Act
        readAllUserTypeUsecase.execute(page, perPage);

        // Assert
        verify(userTypeGateway).findAll(expectedPageable);
    }

    @Test
    void execute_ShouldHandleEmptyList_WhenNoUserTypesExist() {
        // Arrange
        var page = 0;
        var perPage = 10;
        var emptyList = new ArrayList<UserType>();
        var emptyDTOList = new ArrayList<UserTypeDTO>();
        when(userTypeGateway.findAll(any(Pageable.class))).thenReturn(emptyList);
        when(userTypeMapper.toResponseListDTO(anyList())).thenReturn(emptyDTOList);

        // Act
        var result = readAllUserTypeUsecase.execute(page, perPage);

        // Assert
        assertNotNull(result);
        assertEquals(page, result.getPage());
        assertEquals(perPage, result.getPerPage());
        assertEquals(Long.valueOf(0), result.getTotal());
        assertEquals(emptyDTOList, result.getItems());
        verify(userTypeGateway).findAll(any(Pageable.class));
        verify(userTypeMapper).toResponseListDTO(emptyList);
    }

    @Test
    void execute_ShouldPassCorrectParametersToMapper_WhenExecuted() {
        // Arrange
        var page = 1;
        var perPage = 20;
        when(userTypeGateway.findAll(any(Pageable.class))).thenReturn(userTypeList);
        when(userTypeMapper.toResponseListDTO(userTypeList)).thenReturn(userTypeDTOList);

        // Act
        readAllUserTypeUsecase.execute(page, perPage);

        // Assert
        verify(userTypeMapper).toResponseListDTO(userTypeList);
    }

    @Test
    void execute_ShouldCallMethodsInCorrectOrder_WhenExecuted() {
        // Arrange
        var page = 0;
        var perPage = 10;
        when(userTypeGateway.findAll(any(Pageable.class))).thenReturn(userTypeList);
        when(userTypeMapper.toResponseListDTO(anyList())).thenReturn(userTypeDTOList);

        // Act
        readAllUserTypeUsecase.execute(page, perPage);

        // Assert
        var inOrder = inOrder(userTypeGateway, userTypeMapper);
        inOrder.verify(userTypeGateway).findAll(any(Pageable.class));
        inOrder.verify(userTypeMapper).toResponseListDTO(userTypeList);
    }

    @Test
    void execute_ShouldHandleDifferentPageSizes_WhenExecuted() {
        // Arrange
        var pageSizes = new Integer[]{5, 10, 25, 50};
        when(userTypeGateway.findAll(any(Pageable.class))).thenReturn(userTypeList);
        when(userTypeMapper.toResponseListDTO(anyList())).thenReturn(userTypeDTOList);

        // Act & Assert
        for (var pageSize : pageSizes) {
            readAllUserTypeUsecase.execute(0, pageSize);
            verify(userTypeGateway).findAll(PageRequest.of(0, pageSize));
        }
    }

    @Test
    void execute_ShouldSetCorrectTotalFromListSize_WhenExecuted() {
        // Arrange
        var page = 1;
        var perPage = 20;
        var largeList = Arrays.asList(
            new UserType(), new UserType(), new UserType(), new UserType(), new UserType()
        );
        when(userTypeGateway.findAll(any(Pageable.class))).thenReturn(largeList);
        when(userTypeMapper.toResponseListDTO(anyList())).thenReturn(userTypeDTOList);

        // Act
        var result = readAllUserTypeUsecase.execute(page, perPage);

        // Assert
        assertEquals(Long.valueOf(largeList.size()), result.getTotal());
        assertEquals(page, result.getPage());
        assertEquals(perPage, result.getPerPage());
    }

    @Test
    void execute_ShouldCreateNewPaginatedUserTypeDTO_WhenExecuted() {
        // Arrange
        var page = 0;
        var perPage = 10;
        when(userTypeGateway.findAll(any(Pageable.class))).thenReturn(userTypeList);
        when(userTypeMapper.toResponseListDTO(anyList())).thenReturn(userTypeDTOList);

        // Act
        var result = readAllUserTypeUsecase.execute(page, perPage);

        // Assert
        assertNotNull(result);
        assertInstanceOf(PaginatedUserTypeDTO.class, result);
        verify(userTypeGateway).findAll(any(Pageable.class));
        verify(userTypeMapper).toResponseListDTO(userTypeList);
    }

    @Test
    void execute_ShouldHandleZeroPage_WhenExecuted() {
        // Arrange
        var page = 0;
        var perPage = 15;
        when(userTypeGateway.findAll(any(Pageable.class))).thenReturn(userTypeList);
        when(userTypeMapper.toResponseListDTO(anyList())).thenReturn(userTypeDTOList);

        // Act
        var result = readAllUserTypeUsecase.execute(page, perPage);

        // Assert
        assertEquals(page, result.getPage());
        assertEquals(perPage, result.getPerPage());
        verify(userTypeGateway).findAll(PageRequest.of(page, perPage));
    }

    @Test
    void execute_ShouldHandleLargePageNumber_WhenExecuted() {
        // Arrange
        var page = 100;
        var perPage = 5;
        when(userTypeGateway.findAll(any(Pageable.class))).thenReturn(userTypeList);
        when(userTypeMapper.toResponseListDTO(anyList())).thenReturn(userTypeDTOList);

        // Act
        var result = readAllUserTypeUsecase.execute(page, perPage);

        // Assert
        assertEquals(page, result.getPage());
        assertEquals(perPage, result.getPerPage());
        verify(userTypeGateway).findAll(PageRequest.of(page, perPage));
    }

    @Test
    void execute_ShouldHandleSmallPerPageValue_WhenExecuted() {
        // Arrange
        var page = 0;
        var perPage = 1;
        when(userTypeGateway.findAll(any(Pageable.class))).thenReturn(userTypeList);
        when(userTypeMapper.toResponseListDTO(anyList())).thenReturn(userTypeDTOList);

        // Act
        var result = readAllUserTypeUsecase.execute(page, perPage);

        // Assert
        assertEquals(page, result.getPage());
        assertEquals(perPage, result.getPerPage());
        assertEquals(Long.valueOf(userTypeList.size()), result.getTotal());
        verify(userTypeGateway).findAll(PageRequest.of(page, perPage));
    }
}
