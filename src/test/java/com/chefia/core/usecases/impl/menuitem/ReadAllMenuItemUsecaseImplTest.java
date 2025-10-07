package com.chefia.core.usecases.impl.menuitem;

import com.chefia.core.entities.MenuItem;
import com.chefia.core.gateway.MenuItemGateway;
import com.chefia.core.mapper.MenuItemMapper;
import com.chefia.menuitems.model.MenuItemDTO;
import com.chefia.menuitems.model.PaginatedMenuItemDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadAllMenuItemUsecaseImplTest {

    @Mock
    private MenuItemGateway menuItemGateway;

    @Mock
    private MenuItemMapper menuItemMapper;

    @InjectMocks
    private ReadAllMenuItemUsecaseImpl readAllMenuItemUsecase;

    private List<MenuItem> menuItemList;
    private List<MenuItemDTO> menuItemDTOList;

    @BeforeEach
    void setUp() {
        var menuItem1 = new MenuItem();
        menuItem1.setNrSeqMenuItem(1L);
        menuItem1.setName("Margherita Pizza");
        menuItem1.setPrice(12.99);
        menuItem1.setRestaurantId(1L);

        var menuItem2 = new MenuItem();
        menuItem2.setNrSeqMenuItem(2L);
        menuItem2.setName("Caesar Salad");
        menuItem2.setPrice(8.99);
        menuItem2.setRestaurantId(1L);

        menuItemList = Arrays.asList(menuItem1, menuItem2);

        var menuItemDTO1 = new MenuItemDTO();
        var menuItemDTO2 = new MenuItemDTO();
        menuItemDTOList = Arrays.asList(menuItemDTO1, menuItemDTO2);

        readAllMenuItemUsecase = new ReadAllMenuItemUsecaseImpl(menuItemGateway, menuItemMapper);
    }

    @Test
    void execute_ShouldReturnPaginatedMenuItemDTO_WhenMenuItemsExist() {
        // Arrange
        var page = 0;
        var perPage = 10;
        when(menuItemGateway.findAll(any(Pageable.class))).thenReturn(menuItemList);
        when(menuItemMapper.toResponseListDTO(anyList())).thenReturn(menuItemDTOList);

        // Act
        var result = readAllMenuItemUsecase.execute(page, perPage);

        // Assert
        assertNotNull(result);
        assertEquals(page, result.getPage());
        assertEquals(perPage, result.getPerPage());
        assertEquals(Long.valueOf(menuItemList.size()), result.getTotal());
        assertEquals(menuItemDTOList, result.getItems());
        verify(menuItemGateway).findAll(any(Pageable.class));
        verify(menuItemMapper).toResponseListDTO(menuItemList);
    }

    @Test
    void execute_ShouldCallGatewayWithCorrectPageable_WhenExecuted() {
        // Arrange
        var page = 2;
        var perPage = 5;
        var expectedPageable = PageRequest.of(page, perPage);
        when(menuItemGateway.findAll(any(Pageable.class))).thenReturn(menuItemList);
        when(menuItemMapper.toResponseListDTO(anyList())).thenReturn(menuItemDTOList);

        // Act
        readAllMenuItemUsecase.execute(page, perPage);

        // Assert
        verify(menuItemGateway).findAll(expectedPageable);
    }

    @Test
    void execute_ShouldHandleEmptyList_WhenNoMenuItemsExist() {
        // Arrange
        var page = 0;
        var perPage = 10;
        var emptyList = new ArrayList<MenuItem>();
        var emptyDTOList = new ArrayList<MenuItemDTO>();
        when(menuItemGateway.findAll(any(Pageable.class))).thenReturn(emptyList);
        when(menuItemMapper.toResponseListDTO(anyList())).thenReturn(emptyDTOList);

        // Act
        PaginatedMenuItemDTO result = readAllMenuItemUsecase.execute(page, perPage);

        // Assert
        assertNotNull(result);
        assertEquals(page, result.getPage());
        assertEquals(perPage, result.getPerPage());
        assertEquals(Long.valueOf(0), result.getTotal());
        assertEquals(emptyDTOList, result.getItems());
        verify(menuItemGateway).findAll(any(Pageable.class));
        verify(menuItemMapper).toResponseListDTO(emptyList);
    }

    @Test
    void execute_ShouldCallMethodsInCorrectOrder_WhenExecuted() {
        // Arrange
        var page = 0;
        var perPage = 10;
        when(menuItemGateway.findAll(any(Pageable.class))).thenReturn(menuItemList);
        when(menuItemMapper.toResponseListDTO(anyList())).thenReturn(menuItemDTOList);

        // Act
        readAllMenuItemUsecase.execute(page, perPage);

        // Assert
        var inOrder = inOrder(menuItemGateway, menuItemMapper);
        inOrder.verify(menuItemGateway).findAll(any(Pageable.class));
        inOrder.verify(menuItemMapper).toResponseListDTO(menuItemList);
    }

    @Test
    void execute_ShouldSetCorrectTotalFromListSize_WhenExecuted() {
        // Arrange
        var page = 1;
        var perPage = 20;
        var largeList = Arrays.asList(
            new MenuItem(), new MenuItem(), new MenuItem(), new MenuItem(), new MenuItem()
        );
        when(menuItemGateway.findAll(any(Pageable.class))).thenReturn(largeList);
        when(menuItemMapper.toResponseListDTO(anyList())).thenReturn(menuItemDTOList);

        // Act
        var result = readAllMenuItemUsecase.execute(page, perPage);

        // Assert
        assertEquals(Long.valueOf(largeList.size()), result.getTotal());
        assertEquals(page, result.getPage());
        assertEquals(perPage, result.getPerPage());
    }

    @Test
    void execute_ShouldHandleDifferentPageSizes_WhenExecuted() {
        // Arrange
        var page = 3;
        var perPage = 15;
        when(menuItemGateway.findAll(any(Pageable.class))).thenReturn(menuItemList);
        when(menuItemMapper.toResponseListDTO(anyList())).thenReturn(menuItemDTOList);

        // Act
        var result = readAllMenuItemUsecase.execute(page, perPage);

        // Assert
        assertEquals(page, result.getPage());
        assertEquals(perPage, result.getPerPage());
        verify(menuItemGateway).findAll(PageRequest.of(page, perPage));
    }

    @Test
    void execute_ShouldCreateNewPaginatedMenuItemDTO_WhenExecuted() {
        // Arrange
        var page = 0;
        var perPage = 10;
        when(menuItemGateway.findAll(any(Pageable.class))).thenReturn(menuItemList);
        when(menuItemMapper.toResponseListDTO(anyList())).thenReturn(menuItemDTOList);

        // Act
        var result = readAllMenuItemUsecase.execute(page, perPage);

        // Assert
        assertNotNull(result);
        assertInstanceOf(PaginatedMenuItemDTO.class, result);
        verify(menuItemGateway).findAll(any(Pageable.class));
        verify(menuItemMapper).toResponseListDTO(menuItemList);
    }
}
