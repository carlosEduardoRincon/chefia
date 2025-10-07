package com.chefia.core.controllers;

import com.chefia.core.usecases.interfaces.menuitem.*;
import com.chefia.menuitems.model.CreateMenuItemDTO;
import com.chefia.menuitems.model.MenuItemDTO;
import com.chefia.menuitems.model.PaginatedMenuItemDTO;
import com.chefia.menuitems.model.UpdateMenuItemDTO;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuItemControllerTest {

    @Mock
    private CreateMenuItemUsecase createMenuItemUsecase;

    @Mock
    private ReadMenuItemUsecase readMenuItemUsecase;

    @Mock
    private ReadAllMenuItemUsecase readAllMenuItemUsecase;

    @Mock
    private ReadMenuItemsByRestaurantUsecase readMenuItemsByRestaurantUsecase;

    @Mock
    private UpdateMenuItemUsecase updateMenuItemUsecase;

    @Mock
    private DeleteMenuItemUsecase deleteMenuItemUsecase;

    @InjectMocks
    private MenuItemController menuItemController;

    private CreateMenuItemDTO createMenuItemDTO;
    private UpdateMenuItemDTO updateMenuItemDTO;
    private MenuItemDTO menuItemDTO;
    private PaginatedMenuItemDTO paginatedMenuItemDTO;
    private List<MenuItemDTO> menuItemList;

    @BeforeEach
    void setUp() {
        createMenuItemDTO = new CreateMenuItemDTO();
        updateMenuItemDTO = new UpdateMenuItemDTO();
        menuItemDTO = new MenuItemDTO();
        paginatedMenuItemDTO = new PaginatedMenuItemDTO();
        menuItemList = Arrays.asList(menuItemDTO, new MenuItemDTO());

        menuItemController = new MenuItemController(
            createMenuItemUsecase,
            readMenuItemUsecase,
            readAllMenuItemUsecase,
            readMenuItemsByRestaurantUsecase,
            updateMenuItemUsecase,
            deleteMenuItemUsecase
        );
    }

    @Test
    void createMenuItem_ShouldReturnMenuItemDTO_WhenValidData() {
        // Arrange
        when(createMenuItemUsecase.execute(any(CreateMenuItemDTO.class))).thenReturn(menuItemDTO);

        // Act
        var result = menuItemController.createMenuItem(createMenuItemDTO);

        // Assert
        assertNotNull(result);
        assertEquals(menuItemDTO, result);
        verify(createMenuItemUsecase, times(1)).execute(createMenuItemDTO);
    }

    @Test
    void getMenuItem_ShouldReturnMenuItemDTO_WhenValidId() {
        // Arrange
        var menuItemId = 1L;
        when(readMenuItemUsecase.execute(anyLong())).thenReturn(menuItemDTO);

        // Act
        var result = menuItemController.findById(menuItemId);

        // Assert
        assertNotNull(result);
        assertEquals(menuItemDTO, result);
        verify(readMenuItemUsecase, times(1)).execute(menuItemId);
    }

    @Test
    void listMenuItems_ShouldReturnPaginatedMenuItemsDTO_WhenValidParameters() {
        // Arrange
        var page = 1;
        var perPage = 10;
        when(readAllMenuItemUsecase.execute(anyInt(), anyInt())).thenReturn(paginatedMenuItemDTO);

        // Act
        var result = menuItemController.findAll(page, perPage);

        // Assert
        assertNotNull(result);
        assertEquals(paginatedMenuItemDTO, result);
        verify(readAllMenuItemUsecase, times(1)).execute(page, perPage);
    }

    @Test
    void updateMenuItem_ShouldReturnMenuItemDTO_WhenValidData() {
        // Arrange
        var menuItemId = 1L;
        when(updateMenuItemUsecase.execute(anyLong(), any(UpdateMenuItemDTO.class))).thenReturn(menuItemDTO);

        // Act
        var result = menuItemController.updateMenuItem(menuItemId, updateMenuItemDTO);

        // Assert
        assertNotNull(result);
        assertEquals(menuItemDTO, result);
        verify(updateMenuItemUsecase, times(1)).execute(menuItemId, updateMenuItemDTO);
    }

    @Test
    void deleteMenuItem_ShouldCallUsecase_WhenValidId() {
        // Arrange
        var menuItemId = 1L;

        // Act
        menuItemController.deleteMenuItem(menuItemId);

        // Assert
        verify(deleteMenuItemUsecase, times(1)).execute(menuItemId);
    }

    @Test
    void listMenuItemsByRestaurant_ShouldReturnPaginatedMenuItemsDTO_WhenValidParameters() {
        // Arrange
        var page = 0;
        var perPage = 20;
        when(readAllMenuItemUsecase.execute(anyInt(), anyInt())).thenReturn(paginatedMenuItemDTO);

        // Act
        var result = menuItemController.findAll(page, perPage);

        // Assert
        assertNotNull(result);
        assertEquals(paginatedMenuItemDTO, result);
        verify(readAllMenuItemUsecase, times(1)).execute(page, perPage);
    }

    @Test
    void createMenuItem_ShouldPassCorrectParameterToUsecase_WhenCalled() {
        // Arrange
        CreateMenuItemDTO specificDTO = new CreateMenuItemDTO();
        when(createMenuItemUsecase.execute(specificDTO)).thenReturn(menuItemDTO);

        // Act
        menuItemController.createMenuItem(specificDTO);

        // Assert
        verify(createMenuItemUsecase).execute(specificDTO);
    }

    @Test
    void updateMenuItem_ShouldPassCorrectParametersToUsecase_WhenCalled() {
        // Arrange
        var menuItemId = 7L;
        var specificDTO = new UpdateMenuItemDTO();
        when(updateMenuItemUsecase.execute(menuItemId, specificDTO)).thenReturn(menuItemDTO);

        // Act
        menuItemController.updateMenuItem(menuItemId, specificDTO);

        // Assert
        verify(updateMenuItemUsecase).execute(menuItemId, specificDTO);
    }

    @Test
    void findByRestaurantId_ShouldReturnListOfMenuItemDTO_WhenValidRestaurantId() {
        // Arrange
        var restaurantId = 1L;
        when(readMenuItemsByRestaurantUsecase.execute(anyLong())).thenReturn(menuItemList);

        // Act
        var result = menuItemController.findByRestaurantId(restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(menuItemList, result);
        verify(readMenuItemsByRestaurantUsecase, times(1)).execute(restaurantId);
    }

    @Test
    void findByRestaurantId_ShouldReturnEmptyList_WhenRestaurantHasNoMenuItems() {
        // Arrange
        var restaurantId = 2L;
        List<MenuItemDTO> emptyList = List.of();
        when(readMenuItemsByRestaurantUsecase.execute(anyLong())).thenReturn(emptyList);

        // Act
        var result = menuItemController.findByRestaurantId(restaurantId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(readMenuItemsByRestaurantUsecase, times(1)).execute(restaurantId);
    }

    @Test
    void findByRestaurantId_ShouldCallUsecaseWithCorrectParameter_WhenCalled() {
        // Arrange
        var restaurantId = 5L;
        when(readMenuItemsByRestaurantUsecase.execute(restaurantId)).thenReturn(menuItemList);

        // Act
        menuItemController.findByRestaurantId(restaurantId);

        // Assert
        verify(readMenuItemsByRestaurantUsecase).execute(restaurantId);
    }
}
