package com.chefia.core.usecases.impl.menuitem;

import com.chefia.core.entities.MenuItem;
import com.chefia.core.gateway.MenuItemGateway;
import com.chefia.core.mapper.MenuItemMapper;
import com.chefia.menuitems.model.MenuItemDTO;
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
class ReadMenuItemsByRestaurantUsecaseImplTest {

    @Mock
    private MenuItemGateway menuItemGateway;

    @Mock
    private MenuItemMapper menuItemMapper;

    @InjectMocks
    private ReadMenuItemsByRestaurantUsecaseImpl readMenuItemsByRestaurantUsecase;

    private MenuItem menuItem1;
    private MenuItem menuItem2;
    private MenuItemDTO menuItemDTO1;
    private MenuItemDTO menuItemDTO2;
    private Long restaurantId;

    @BeforeEach
    void setUp() {
        restaurantId = 1L;

        menuItem1 = new MenuItem();
        menuItem1.setNrSeqMenuItem(1L);
        menuItem1.setName("Hambúrguer");
        menuItem1.setRestaurantId(restaurantId);

        menuItem2 = new MenuItem();
        menuItem2.setNrSeqMenuItem(2L);
        menuItem2.setName("Pizza");
        menuItem2.setRestaurantId(restaurantId);

        menuItemDTO1 = new MenuItemDTO();
        menuItemDTO1.setId(1L);
        menuItemDTO1.setName("Hambúrguer");
        menuItemDTO1.setRestaurantId(restaurantId);

        menuItemDTO2 = new MenuItemDTO();
        menuItemDTO2.setId(2L);
        menuItemDTO2.setName("Pizza");
        menuItemDTO2.setRestaurantId(restaurantId);
    }

    @Test
    void execute_ShouldReturnListOfMenuItemDTO_WhenRestaurantHasMenuItems() {
        // Arrange
        List<MenuItem> menuItems = Arrays.asList(menuItem1, menuItem2);
        when(menuItemGateway.findByRestaurantId(restaurantId)).thenReturn(menuItems);
        when(menuItemMapper.toMenuItemResponseDTO(menuItem1)).thenReturn(menuItemDTO1);
        when(menuItemMapper.toMenuItemResponseDTO(menuItem2)).thenReturn(menuItemDTO2);

        // Act
        List<MenuItemDTO> result = readMenuItemsByRestaurantUsecase.execute(restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(menuItemDTO1, result.get(0));
        assertEquals(menuItemDTO2, result.get(1));
        verify(menuItemGateway, times(1)).findByRestaurantId(restaurantId);
        verify(menuItemMapper, times(1)).toMenuItemResponseDTO(menuItem1);
        verify(menuItemMapper, times(1)).toMenuItemResponseDTO(menuItem2);
    }

    @Test
    void execute_ShouldReturnEmptyList_WhenRestaurantHasNoMenuItems() {
        // Arrange
        List<MenuItem> emptyList = List.of();
        when(menuItemGateway.findByRestaurantId(restaurantId)).thenReturn(emptyList);

        // Act
        List<MenuItemDTO> result = readMenuItemsByRestaurantUsecase.execute(restaurantId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(menuItemGateway, times(1)).findByRestaurantId(restaurantId);
        verify(menuItemMapper, never()).toMenuItemResponseDTO(any());
    }

    @Test
    void execute_ShouldCallGatewayWithCorrectParameter_WhenCalled() {
        // Arrange
        List<MenuItem> menuItems = List.of();
        when(menuItemGateway.findByRestaurantId(restaurantId)).thenReturn(menuItems);

        // Act
        readMenuItemsByRestaurantUsecase.execute(restaurantId);

        // Assert
        verify(menuItemGateway).findByRestaurantId(restaurantId);
    }

    @Test
    void execute_ShouldMapAllMenuItems_WhenMultipleItemsFound() {
        // Arrange
        MenuItem menuItem3 = new MenuItem();
        menuItem3.setNrSeqMenuItem(3L);
        menuItem3.setName("Salada");
        menuItem3.setRestaurantId(restaurantId);

        MenuItemDTO menuItemDTO3 = new MenuItemDTO();
        menuItemDTO3.setId(3L);
        menuItemDTO3.setName("Salada");
        menuItemDTO3.setRestaurantId(restaurantId);

        List<MenuItem> menuItems = Arrays.asList(menuItem1, menuItem2, menuItem3);
        when(menuItemGateway.findByRestaurantId(restaurantId)).thenReturn(menuItems);
        when(menuItemMapper.toMenuItemResponseDTO(menuItem1)).thenReturn(menuItemDTO1);
        when(menuItemMapper.toMenuItemResponseDTO(menuItem2)).thenReturn(menuItemDTO2);
        when(menuItemMapper.toMenuItemResponseDTO(menuItem3)).thenReturn(menuItemDTO3);

        // Act
        List<MenuItemDTO> result = readMenuItemsByRestaurantUsecase.execute(restaurantId);

        // Assert
        assertEquals(3, result.size());
        verify(menuItemMapper, times(3)).toMenuItemResponseDTO(any(MenuItem.class));
    }

    @Test
    void execute_ShouldReturnCorrectOrder_WhenMenuItemsFound() {
        // Arrange
        List<MenuItem> menuItems = Arrays.asList(menuItem1, menuItem2);
        when(menuItemGateway.findByRestaurantId(restaurantId)).thenReturn(menuItems);
        when(menuItemMapper.toMenuItemResponseDTO(menuItem1)).thenReturn(menuItemDTO1);
        when(menuItemMapper.toMenuItemResponseDTO(menuItem2)).thenReturn(menuItemDTO2);

        // Act
        List<MenuItemDTO> result = readMenuItemsByRestaurantUsecase.execute(restaurantId);

        // Assert
        assertEquals(menuItemDTO1.getName(), result.get(0).getName());
        assertEquals(menuItemDTO2.getName(), result.get(1).getName());
    }
}
