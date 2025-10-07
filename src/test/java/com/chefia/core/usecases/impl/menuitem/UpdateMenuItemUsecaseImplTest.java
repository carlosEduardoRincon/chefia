package com.chefia.core.usecases.impl.menuitem;

import com.chefia.core.entities.MenuItem;
import com.chefia.core.exceptions.MenuItemNotFoundException;
import com.chefia.core.gateway.MenuItemGateway;
import com.chefia.core.mapper.MenuItemMapper;
import com.chefia.menuitems.model.MenuItemDTO;
import com.chefia.menuitems.model.UpdateMenuItemDTO;
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
class UpdateMenuItemUsecaseImplTest {

    @Mock
    private MenuItemGateway menuItemGateway;

    @Mock
    private MenuItemMapper menuItemMapper;

    @InjectMocks
    private UpdateMenuItemUsecaseImpl updateMenuItemUsecase;

    private MenuItem existingMenuItem;
    private UpdateMenuItemDTO updateMenuItemDTO;
    private MenuItemDTO menuItemDTO;

    @BeforeEach
    void setUp() {
        existingMenuItem = new MenuItem();
        existingMenuItem.setNrSeqMenuItem(1L);
        existingMenuItem.setName("Old Burger");
        existingMenuItem.setDescription("Old burger description");
        existingMenuItem.setPrice(10.99);
        existingMenuItem.setRestaurantId(1L);

        updateMenuItemDTO = new UpdateMenuItemDTO();
        menuItemDTO = new MenuItemDTO();

        updateMenuItemUsecase = new UpdateMenuItemUsecaseImpl(menuItemGateway, menuItemMapper);
    }

    @Test
    void execute_ShouldReturnUpdatedMenuItemDTO_WhenMenuItemExists() {
        // Arrange
        var menuItemId = 1L;
        var newName = "Updated Burger";
        var newDescription = "Updated description";
        var newPrice = 15.99;
        var newImagePath = "new-image.jpg";

        updateMenuItemDTO.setName(newName);
        updateMenuItemDTO.setDescription(newDescription);
        updateMenuItemDTO.setPrice(newPrice);
        updateMenuItemDTO.setAvailableOnlyOnSite(false);
        updateMenuItemDTO.setImagePath(newImagePath);

        when(menuItemGateway.findByMenuItemId(anyLong())).thenReturn(Optional.of(existingMenuItem));
        when(menuItemMapper.toMenuItemResponseDTO(any(MenuItem.class))).thenReturn(menuItemDTO);

        // Act
        var result = updateMenuItemUsecase.execute(menuItemId, updateMenuItemDTO);

        // Assert
        assertNotNull(result);
        assertEquals(menuItemDTO, result);
        verify(menuItemGateway).findByMenuItemId(menuItemId);
        verify(menuItemGateway).update(menuItemId, existingMenuItem);
        verify(menuItemMapper).toMenuItemResponseDTO(existingMenuItem);

        // Verify that the entity was updated with new values
        assertEquals(newName, existingMenuItem.getName());
        assertEquals(newDescription, existingMenuItem.getDescription());
        assertEquals(newPrice, existingMenuItem.getPrice());
        assertEquals(false, existingMenuItem.getAvailableOnlyOnSite());
        assertEquals(newImagePath, existingMenuItem.getImagePath());
    }

    @Test
    void execute_ShouldThrowMenuItemNotFoundException_WhenMenuItemDoesNotExist() {
        // Arrange
        var menuItemId = 999L;
        when(menuItemGateway.findByMenuItemId(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(MenuItemNotFoundException.class,
                    () -> updateMenuItemUsecase.execute(menuItemId, updateMenuItemDTO));

        assertEquals("Menu Item not found with id: " + menuItemId, exception.getMessage());
        verify(menuItemGateway).findByMenuItemId(menuItemId);
        verify(menuItemGateway, never()).update(anyLong(), any(MenuItem.class));
        verify(menuItemMapper, never()).toMenuItemResponseDTO(any(MenuItem.class));
    }

    @Test
    void execute_ShouldCallUpdateOnGateway_WhenMenuItemExists() {
        // Arrange
        var menuItemId = 1L;
        updateMenuItemDTO.setName("New Name");
        when(menuItemGateway.findByMenuItemId(anyLong())).thenReturn(Optional.of(existingMenuItem));
        when(menuItemMapper.toMenuItemResponseDTO(any(MenuItem.class))).thenReturn(menuItemDTO);

        // Act
        updateMenuItemUsecase.execute(menuItemId, updateMenuItemDTO);

        // Assert
        verify(menuItemGateway, times(1)).update(menuItemId, existingMenuItem);
    }

    @Test
    void execute_ShouldCallMethodsInCorrectOrder_WhenExecuted() {
        // Arrange
        var menuItemId = 1L;
        updateMenuItemDTO.setName("Test Name");
        when(menuItemGateway.findByMenuItemId(anyLong())).thenReturn(Optional.of(existingMenuItem));
        when(menuItemMapper.toMenuItemResponseDTO(any(MenuItem.class))).thenReturn(menuItemDTO);

        // Act
        updateMenuItemUsecase.execute(menuItemId, updateMenuItemDTO);

        // Assert
        var inOrder = inOrder(menuItemGateway, menuItemMapper);
        inOrder.verify(menuItemGateway).findByMenuItemId(menuItemId);
        inOrder.verify(menuItemGateway).update(menuItemId, existingMenuItem);
        inOrder.verify(menuItemMapper).toMenuItemResponseDTO(existingMenuItem);
    }

    @Test
    void execute_ShouldUpdateEntityFieldsDirectly_WhenExecuted() {
        // Arrange
        var menuItemId = 1L;
        var expectedName = "Direct Update Name";
        var expectedDescription = "Direct Update Description";
        var expectedPrice = 25.50;
        var expectedAvailableOnlyOnSite = true;
        var expectedImagePath = "direct-update.jpg";

        updateMenuItemDTO.setName(expectedName);
        updateMenuItemDTO.setDescription(expectedDescription);
        updateMenuItemDTO.setPrice(expectedPrice);
        updateMenuItemDTO.setAvailableOnlyOnSite(expectedAvailableOnlyOnSite);
        updateMenuItemDTO.setImagePath(expectedImagePath);

        when(menuItemGateway.findByMenuItemId(menuItemId)).thenReturn(Optional.of(existingMenuItem));
        when(menuItemMapper.toMenuItemResponseDTO(any(MenuItem.class))).thenReturn(menuItemDTO);

        // Act
        updateMenuItemUsecase.execute(menuItemId, updateMenuItemDTO);

        // Assert
        assertEquals(expectedName, existingMenuItem.getName());
        assertEquals(expectedDescription, existingMenuItem.getDescription());
        assertEquals(expectedPrice, existingMenuItem.getPrice());
        assertEquals(expectedAvailableOnlyOnSite, existingMenuItem.getAvailableOnlyOnSite());
        assertEquals(expectedImagePath, existingMenuItem.getImagePath());
    }

    @Test
    void execute_ShouldCallFindByMenuItemIdWithCorrectParameter_WhenExecuted() {
        // Arrange
        var specificId = 7L;
        when(menuItemGateway.findByMenuItemId(specificId)).thenReturn(Optional.of(existingMenuItem));
        when(menuItemMapper.toMenuItemResponseDTO(any(MenuItem.class))).thenReturn(menuItemDTO);

        // Act
        updateMenuItemUsecase.execute(specificId, updateMenuItemDTO);

        // Assert
        verify(menuItemGateway).findByMenuItemId(specificId);
    }

    @Test
    void execute_ShouldUpdatePriceCorrectly_WhenPriceIsChanged() {
        // Arrange
        var menuItemId = 1L;
        var newPrice = 18.99;
        updateMenuItemDTO.setPrice(newPrice);

        when(menuItemGateway.findByMenuItemId(menuItemId)).thenReturn(Optional.of(existingMenuItem));
        when(menuItemMapper.toMenuItemResponseDTO(existingMenuItem)).thenReturn(menuItemDTO);

        // Act
        updateMenuItemUsecase.execute(menuItemId, updateMenuItemDTO);

        // Assert
        assertEquals(newPrice, existingMenuItem.getPrice());
        verify(menuItemGateway).update(menuItemId, existingMenuItem);
        verify(menuItemMapper).toMenuItemResponseDTO(existingMenuItem);
    }

    @Test
    void execute_ShouldUpdateNameCorrectly_WhenNameIsChanged() {
        // Arrange
        var menuItemId = 1L;
        var newName = "New MenuItem Name";
        updateMenuItemDTO.setName(newName);

        when(menuItemGateway.findByMenuItemId(menuItemId)).thenReturn(Optional.of(existingMenuItem));
        when(menuItemMapper.toMenuItemResponseDTO(existingMenuItem)).thenReturn(menuItemDTO);

        // Act
        updateMenuItemUsecase.execute(menuItemId, updateMenuItemDTO);

        // Assert
        assertEquals(newName, existingMenuItem.getName());
        verify(menuItemGateway).update(menuItemId, existingMenuItem);
        verify(menuItemMapper).toMenuItemResponseDTO(existingMenuItem);
    }

    @Test
    void execute_ShouldUpdateDescriptionCorrectly_WhenDescriptionIsChanged() {
        // Arrange
        var menuItemId = 1L;
        var newDescription = "New description for menu item";
        updateMenuItemDTO.setDescription(newDescription);

        when(menuItemGateway.findByMenuItemId(menuItemId)).thenReturn(Optional.of(existingMenuItem));
        when(menuItemMapper.toMenuItemResponseDTO(existingMenuItem)).thenReturn(menuItemDTO);

        // Act
        updateMenuItemUsecase.execute(menuItemId, updateMenuItemDTO);

        // Assert
        assertEquals(newDescription, existingMenuItem.getDescription());
        verify(menuItemGateway).update(menuItemId, existingMenuItem);
        verify(menuItemMapper).toMenuItemResponseDTO(existingMenuItem);
    }

    @Test
    void execute_ShouldUpdateAvailableOnlyOnSiteCorrectly_WhenAvailabilityIsChanged() {
        // Arrange
        var menuItemId = 1L;
        var newAvailability = true;
        updateMenuItemDTO.setAvailableOnlyOnSite(newAvailability);

        when(menuItemGateway.findByMenuItemId(menuItemId)).thenReturn(Optional.of(existingMenuItem));
        when(menuItemMapper.toMenuItemResponseDTO(existingMenuItem)).thenReturn(menuItemDTO);

        // Act
        updateMenuItemUsecase.execute(menuItemId, updateMenuItemDTO);

        // Assert
        assertEquals(newAvailability, existingMenuItem.getAvailableOnlyOnSite());
        verify(menuItemGateway).update(menuItemId, existingMenuItem);
        verify(menuItemMapper).toMenuItemResponseDTO(existingMenuItem);
    }

    @Test
    void execute_ShouldUpdateImagePathCorrectly_WhenImagePathIsChanged() {
        // Arrange
        var menuItemId = 1L;
        var newImagePath = "path/to/new/image.jpg";
        updateMenuItemDTO.setImagePath(newImagePath);

        when(menuItemGateway.findByMenuItemId(menuItemId)).thenReturn(Optional.of(existingMenuItem));
        when(menuItemMapper.toMenuItemResponseDTO(existingMenuItem)).thenReturn(menuItemDTO);

        // Act
        updateMenuItemUsecase.execute(menuItemId, updateMenuItemDTO);

        // Assert
        assertEquals(newImagePath, existingMenuItem.getImagePath());
        verify(menuItemGateway).update(menuItemId, existingMenuItem);
        verify(menuItemMapper).toMenuItemResponseDTO(existingMenuItem);
    }

    @Test
    void execute_ShouldPassCorrectEntityToMapper_WhenMappingResponse() {
        // Arrange
        var menuItemId = 3L;
        var specificMenuItem = new MenuItem();
        specificMenuItem.setNrSeqMenuItem(menuItemId);
        specificMenuItem.setName("Specific MenuItem");
        specificMenuItem.setDescription("Specific description");

        updateMenuItemDTO.setName("Updated Name");

        when(menuItemGateway.findByMenuItemId(menuItemId)).thenReturn(Optional.of(specificMenuItem));
        when(menuItemMapper.toMenuItemResponseDTO(specificMenuItem)).thenReturn(menuItemDTO);

        // Act
        updateMenuItemUsecase.execute(menuItemId, updateMenuItemDTO);

        // Assert
        verify(menuItemMapper).toMenuItemResponseDTO(specificMenuItem);
        assertEquals("Updated Name", specificMenuItem.getName());
    }

    @Test
    void execute_ShouldHandleNullValues_WhenDTOHasNullFields() {
        // Arrange
        var menuItemId = 1L;
        updateMenuItemDTO.setName(null);
        updateMenuItemDTO.setDescription(null);
        updateMenuItemDTO.setPrice(null);
        updateMenuItemDTO.setImagePath(null);

        when(menuItemGateway.findByMenuItemId(menuItemId)).thenReturn(Optional.of(existingMenuItem));
        when(menuItemMapper.toMenuItemResponseDTO(any(MenuItem.class))).thenReturn(menuItemDTO);

        // Act
        updateMenuItemUsecase.execute(menuItemId, updateMenuItemDTO);

        // Assert
        assertNull(existingMenuItem.getName());
        assertNull(existingMenuItem.getDescription());
        assertNull(existingMenuItem.getPrice());
        assertNull(existingMenuItem.getImagePath());
        verify(menuItemGateway).update(menuItemId, existingMenuItem);
    }
}
