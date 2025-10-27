package com.chefia.core.usecases.impl.menuitem;

import com.chefia.core.entities.MenuItem;
import com.chefia.core.exceptions.MenuItemNotFoundException;
import com.chefia.core.gateway.MenuItemGateway;
import com.chefia.core.mapper.MenuItemMapper;
import com.chefia.menuitems.model.MenuItemDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadMenuItemUsecaseImplTest {

    @Mock
    private MenuItemGateway menuItemGateway;

    @Mock
    private MenuItemMapper menuItemMapper;

    @InjectMocks
    private ReadMenuItemUsecaseImpl readMenuItemUsecase;

    private MenuItem menuItem;
    private MenuItemDTO menuItemDTO;

    @BeforeEach
    void setUp() {
        menuItem = new MenuItem();
        menuItem.setNrSeqMenuItem(1L);
        menuItem.setName("Margherita Pizza");
        menuItem.setDescription("Classic pizza with tomato, mozzarella and basil");
        menuItem.setPrice(12.99);
        menuItem.setAvailableOnlyOnSite(true);
        menuItem.setRestaurantId(5L);

        menuItemDTO = new MenuItemDTO();

        readMenuItemUsecase = new ReadMenuItemUsecaseImpl(menuItemGateway, menuItemMapper);
    }

    @Test
    void execute_ShouldReturnMenuItemDTO_WhenMenuItemExists() {
        // Arrange
        var menuItemId = 1L;
        when(menuItemGateway.findByMenuItemId(anyLong())).thenReturn(Optional.of(menuItem));
        when(menuItemMapper.toMenuItemResponseDTO(any(MenuItem.class))).thenReturn(menuItemDTO);

        // Act
        var result = readMenuItemUsecase.execute(menuItemId);

        // Assert
        assertNotNull(result);
        assertEquals(menuItemDTO, result);
        verify(menuItemGateway).findByMenuItemId(menuItemId);
        verify(menuItemMapper).toMenuItemResponseDTO(menuItem);
    }

    @Test
    void execute_ShouldThrowMenuItemNotFoundException_WhenMenuItemDoesNotExist() {
        // Arrange
        var menuItemId = 999L;
        when(menuItemGateway.findByMenuItemId(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MenuItemNotFoundException.class, 
                    () -> readMenuItemUsecase.execute(menuItemId));

        verify(menuItemGateway).findByMenuItemId(menuItemId);
        verify(menuItemMapper, never()).toMenuItemResponseDTO(any(MenuItem.class));
    }

    @Test
    void execute_ShouldCallGatewayWithCorrectId_WhenExecuted() {
        // Arrange
        var specificId = 5L;
        when(menuItemGateway.findByMenuItemId(specificId)).thenReturn(Optional.of(menuItem));
        when(menuItemMapper.toMenuItemResponseDTO(any(MenuItem.class))).thenReturn(menuItemDTO);

        // Act
        readMenuItemUsecase.execute(specificId);

        // Assert
        verify(menuItemGateway).findByMenuItemId(specificId);
    }

    @Test
    void execute_ShouldCallMapperOnce_WhenMenuItemExists() {
        // Arrange
        var menuItemId = 1L;
        when(menuItemGateway.findByMenuItemId(anyLong())).thenReturn(Optional.of(menuItem));
        when(menuItemMapper.toMenuItemResponseDTO(any(MenuItem.class))).thenReturn(menuItemDTO);

        // Act
        readMenuItemUsecase.execute(menuItemId);

        // Assert
        verify(menuItemMapper, times(1)).toMenuItemResponseDTO(menuItem);
    }

    @Test
    void execute_ShouldPassCorrectEntityToMapper_WhenMappingResponse() {
        // Arrange
        var menuItemId = 1L;
        var specificMenuItem = new MenuItem();
        specificMenuItem.setNrSeqMenuItem(menuItemId);
        specificMenuItem.setName("Caesar Salad");
        specificMenuItem.setPrice(8.99);
        specificMenuItem.setRestaurantId(3L);

        when(menuItemGateway.findByMenuItemId(menuItemId)).thenReturn(Optional.of(specificMenuItem));
        when(menuItemMapper.toMenuItemResponseDTO(specificMenuItem)).thenReturn(menuItemDTO);

        // Act
        readMenuItemUsecase.execute(menuItemId);

        // Assert
        verify(menuItemMapper).toMenuItemResponseDTO(specificMenuItem);
    }

    @Test
    void execute_ShouldThrowExceptionWithCorrectMessage_WhenMenuItemNotFound() {
        // Arrange
        var menuItemId = 123L;
        when(menuItemGateway.findByMenuItemId(menuItemId)).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(MenuItemNotFoundException.class,
                () -> readMenuItemUsecase.execute(menuItemId));

        assertEquals("Menu Item not found with id: " + menuItemId, exception.getMessage());
        verify(menuItemGateway).findByMenuItemId(menuItemId);
        verify(menuItemMapper, never()).toMenuItemResponseDTO(any(MenuItem.class));
    }

    @Test
    void execute_ShouldUseOptionalCorrectly_WhenMenuItemExists() {
        // Arrange
        var menuItemId = 1L;
        when(menuItemGateway.findByMenuItemId(menuItemId)).thenReturn(Optional.of(menuItem));
        when(menuItemMapper.toMenuItemResponseDTO(menuItem)).thenReturn(menuItemDTO);

        // Act
        var result = readMenuItemUsecase.execute(menuItemId);

        // Assert
        assertNotNull(result);
        assertEquals(menuItemDTO, result);
        verify(menuItemGateway).findByMenuItemId(menuItemId);
        verify(menuItemMapper).toMenuItemResponseDTO(menuItem);
    }
}
