package com.chefia.core.usecases.impl.user;

import com.chefia.core.entities.User;
import com.chefia.core.gateway.UserGateway;
import com.chefia.core.mapper.UserMapper;
import com.chefia.users.model.PaginatedUsersDTO;
import com.chefia.users.model.UserDTO;
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
class ReadAllUserUsecaseImplTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private ReadAllUserUsecaseImpl readAllUserUsecase;

    private List<User> userList;
    private List<UserDTO> userDTOList;

    @BeforeEach
    void setUp() {
        var user1 = new User();
        user1.setNrSeqUser(1L);
        user1.setName("John Doe");
        user1.setEmail("john@example.com");
        user1.setActive(true);

        var user2 = new User();
        user2.setNrSeqUser(2L);
        user2.setName("Jane Smith");
        user2.setEmail("jane@example.com");
        user2.setActive(true);

        userList = Arrays.asList(user1, user2);

        var userDTO1 = new UserDTO();
        var userDTO2 = new UserDTO();
        userDTOList = Arrays.asList(userDTO1, userDTO2);

        readAllUserUsecase = new ReadAllUserUsecaseImpl(userGateway, userMapper);
    }

    @Test
    void execute_ShouldReturnPaginatedUsersDTO_WhenUsersExist() {
        // Arrange
        var page = 0;
        var perPage = 10;
        when(userGateway.findAll(any(Pageable.class))).thenReturn(userList);
        when(userMapper.toResponseListDTO(anyList())).thenReturn(userDTOList);

        // Act
        var result = readAllUserUsecase.execute(page, perPage);

        // Assert
        assertNotNull(result);
        assertEquals(page, result.getPage());
        assertEquals(perPage, result.getPerPage());
        assertEquals(Long.valueOf(userList.size()), result.getTotal());
        assertEquals(userDTOList, result.getItems());
        verify(userGateway).findAll(any(Pageable.class));
        verify(userMapper).toResponseListDTO(userList);
    }

    @Test
    void execute_ShouldCallGatewayWithCorrectPageable_WhenExecuted() {
        // Arrange
        var page = 2;
        var perPage = 5;
        var expectedPageable = PageRequest.of(page, perPage);
        when(userGateway.findAll(any(Pageable.class))).thenReturn(userList);
        when(userMapper.toResponseListDTO(anyList())).thenReturn(userDTOList);

        // Act
        readAllUserUsecase.execute(page, perPage);

        // Assert
        verify(userGateway).findAll(expectedPageable);
    }

    @Test
    void execute_ShouldHandleEmptyList_WhenNoUsersExist() {
        // Arrange
        var page = 0;
        var perPage = 10;
        var emptyList = new ArrayList<User>();
        var emptyDTOList = new ArrayList<UserDTO>();
        when(userGateway.findAll(any(Pageable.class))).thenReturn(emptyList);
        when(userMapper.toResponseListDTO(anyList())).thenReturn(emptyDTOList);

        // Act
        var result = readAllUserUsecase.execute(page, perPage);

        // Assert
        assertNotNull(result);
        assertEquals(page, result.getPage());
        assertEquals(perPage, result.getPerPage());
        assertEquals(Long.valueOf(0), result.getTotal());
        assertEquals(emptyDTOList, result.getItems());
        verify(userGateway).findAll(any(Pageable.class));
        verify(userMapper).toResponseListDTO(emptyList);
    }

    @Test
    void execute_ShouldPassCorrectParametersToMapper_WhenExecuted() {
        // Arrange
        var page = 1;
        var perPage = 20;
        when(userGateway.findAll(any(Pageable.class))).thenReturn(userList);
        when(userMapper.toResponseListDTO(userList)).thenReturn(userDTOList);

        // Act
        readAllUserUsecase.execute(page, perPage);

        // Assert
        verify(userMapper).toResponseListDTO(userList);
    }

    @Test
    void execute_ShouldCallMethodsInCorrectOrder_WhenExecuted() {
        // Arrange
        var page = 0;
        var perPage = 10;
        when(userGateway.findAll(any(Pageable.class))).thenReturn(userList);
        when(userMapper.toResponseListDTO(anyList())).thenReturn(userDTOList);

        // Act
        readAllUserUsecase.execute(page, perPage);

        // Assert
        var inOrder = inOrder(userGateway, userMapper);
        inOrder.verify(userGateway).findAll(any(Pageable.class));
        inOrder.verify(userMapper).toResponseListDTO(userList);
    }

    @Test
    void execute_ShouldHandleDifferentPageSizes_WhenExecuted() {
        // Arrange
        var pageSizes = new Integer[]{5, 10, 25, 50};
        when(userGateway.findAll(any(Pageable.class))).thenReturn(userList);
        when(userMapper.toResponseListDTO(anyList())).thenReturn(userDTOList);

        // Act & Assert
        for (var pageSize : pageSizes) {
            readAllUserUsecase.execute(0, pageSize);
            verify(userGateway).findAll(PageRequest.of(0, pageSize));
        }
    }

    @Test
    void execute_ShouldSetCorrectTotalFromListSize_WhenExecuted() {
        // Arrange
        var page = 1;
        var perPage = 20;
        var largeList = Arrays.asList(
            new User(), new User(), new User(), new User(), new User()
        );
        when(userGateway.findAll(any(Pageable.class))).thenReturn(largeList);
        when(userMapper.toResponseListDTO(anyList())).thenReturn(userDTOList);

        // Act
        var result = readAllUserUsecase.execute(page, perPage);

        // Assert
        assertEquals(Long.valueOf(largeList.size()), result.getTotal());
        assertEquals(page, result.getPage());
        assertEquals(perPage, result.getPerPage());
    }

    @Test
    void execute_ShouldCreateNewPaginatedUsersDTO_WhenExecuted() {
        // Arrange
        var page = 0;
        var perPage = 10;
        when(userGateway.findAll(any(Pageable.class))).thenReturn(userList);
        when(userMapper.toResponseListDTO(anyList())).thenReturn(userDTOList);

        // Act
        var result = readAllUserUsecase.execute(page, perPage);

        // Assert
        assertNotNull(result);
        assertInstanceOf(PaginatedUsersDTO.class, result);
        verify(userGateway).findAll(any(Pageable.class));
        verify(userMapper).toResponseListDTO(userList);
    }

    @Test
    void execute_ShouldHandleZeroPage_WhenExecuted() {
        // Arrange
        var page = 0;
        var perPage = 15;
        when(userGateway.findAll(any(Pageable.class))).thenReturn(userList);
        when(userMapper.toResponseListDTO(anyList())).thenReturn(userDTOList);

        // Act
        var result = readAllUserUsecase.execute(page, perPage);

        // Assert
        assertEquals(page, result.getPage());
        assertEquals(perPage, result.getPerPage());
        verify(userGateway).findAll(PageRequest.of(page, perPage));
    }

    @Test
    void execute_ShouldHandleLargePageNumber_WhenExecuted() {
        // Arrange
        var page = 100;
        var perPage = 5;
        when(userGateway.findAll(any(Pageable.class))).thenReturn(userList);
        when(userMapper.toResponseListDTO(anyList())).thenReturn(userDTOList);

        // Act
        var result = readAllUserUsecase.execute(page, perPage);

        // Assert
        assertEquals(page, result.getPage());
        assertEquals(perPage, result.getPerPage());
        verify(userGateway).findAll(PageRequest.of(page, perPage));
    }

    @Test
    void execute_ShouldHandleSmallPerPageValue_WhenExecuted() {
        // Arrange
        var page = 0;
        var perPage = 1;
        when(userGateway.findAll(any(Pageable.class))).thenReturn(userList);
        when(userMapper.toResponseListDTO(anyList())).thenReturn(userDTOList);

        // Act
        var result = readAllUserUsecase.execute(page, perPage);

        // Assert
        assertEquals(page, result.getPage());
        assertEquals(perPage, result.getPerPage());
        assertEquals(Long.valueOf(userList.size()), result.getTotal());
        verify(userGateway).findAll(PageRequest.of(page, perPage));
    }
}
