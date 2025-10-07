package com.chefia.core.mapper;

import com.chefia.core.entities.MenuItem;
import com.chefia.menuitems.model.CreateMenuItemDTO;
import com.chefia.menuitems.model.MenuItemDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MenuItemMapperTest {

    @InjectMocks
    private MenuItemMapper menuItemMapper;

    private CreateMenuItemDTO createMenuItemDTO;
    private MenuItem menuItem;

    @BeforeEach
    void setUp() {
        createMenuItemDTO = new CreateMenuItemDTO();
        createMenuItemDTO.setName("Hambúrguer Clássico");
        createMenuItemDTO.setDescription("Delicioso hambúrguer com carne bovina");
        createMenuItemDTO.setPrice(25.90);
        createMenuItemDTO.setAvailableOnlyOnSite(false);
        createMenuItemDTO.setImagePath("images/hamburger.jpg");
        createMenuItemDTO.setRestaurantId(1L);

        menuItem = new MenuItem();
        menuItem.setNrSeqMenuItem(1L);
        menuItem.setName("Hambúrguer Clássico");
        menuItem.setDescription("Delicioso hambúrguer com carne bovina");
        menuItem.setPrice(25.90);
        menuItem.setAvailableOnlyOnSite(false);
        menuItem.setImagePath("images/hamburger.jpg");
        menuItem.setRestaurantId(1L);
    }

    @Test
    void toEntity_ShouldReturnMenuItem_WhenValidCreateMenuItemDTO() {
        // Act
        var result = menuItemMapper.toEntity(createMenuItemDTO);

        // Assert
        assertNotNull(result);
        assertEquals(createMenuItemDTO.getName(), result.getName());
        assertEquals(createMenuItemDTO.getDescription(), result.getDescription());
        assertEquals(createMenuItemDTO.getPrice(), result.getPrice());
        assertEquals(createMenuItemDTO.isAvailableOnlyOnSite(), result.getAvailableOnlyOnSite());
        assertEquals(createMenuItemDTO.getImagePath(), result.getImagePath());
        assertEquals(createMenuItemDTO.getRestaurantId(), result.getRestaurantId());
    }

    @Test
    void toEntity_ShouldMapAllFields_WhenCalled() {
        // Arrange
        createMenuItemDTO.setName("Pizza Margherita");
        createMenuItemDTO.setDescription("Pizza tradicional italiana");
        createMenuItemDTO.setPrice(32.50);
        createMenuItemDTO.setAvailableOnlyOnSite(true);
        createMenuItemDTO.setImagePath("images/pizza-margherita.jpg");
        createMenuItemDTO.setRestaurantId(2L);

        // Act
        var result = menuItemMapper.toEntity(createMenuItemDTO);

        // Assert
        assertEquals("Pizza Margherita", result.getName());
        assertEquals("Pizza tradicional italiana", result.getDescription());
        assertEquals(32.50, result.getPrice());
        assertTrue(result.getAvailableOnlyOnSite());
        assertEquals("images/pizza-margherita.jpg", result.getImagePath());
        assertEquals(2L, result.getRestaurantId());
    }

    @Test
    void toEntity_ShouldHandleNullValues_WhenDTOHasNullFields() {
        // Arrange
        createMenuItemDTO.setName(null);
        createMenuItemDTO.setDescription(null);
        createMenuItemDTO.setPrice(null);
        createMenuItemDTO.setImagePath(null);

        // Act
        var result = menuItemMapper.toEntity(createMenuItemDTO);

        // Assert
        assertNotNull(result);
        assertNull(result.getName());
        assertNull(result.getDescription());
        assertNull(result.getPrice());
        assertNull(result.getImagePath());
        assertEquals(createMenuItemDTO.getRestaurantId(), result.getRestaurantId());
    }

    @Test
    void toEntity_ShouldHandleZeroPrice_WhenPriceIsZero() {
        // Arrange
        createMenuItemDTO.setPrice(0.0);

        // Act
        var result = menuItemMapper.toEntity(createMenuItemDTO);

        // Assert
        assertEquals(0.0, result.getPrice());
    }

    @Test
    void toEntity_ShouldHandleAvailableOnlyOnSiteFalse_WhenSetToFalse() {
        // Arrange
        createMenuItemDTO.setAvailableOnlyOnSite(false);

        // Act
        var result = menuItemMapper.toEntity(createMenuItemDTO);

        // Assert
        assertFalse(result.getAvailableOnlyOnSite());
    }

    @Test
    void toEntity_ShouldHandleAvailableOnlyOnSiteTrue_WhenSetToTrue() {
        // Arrange
        createMenuItemDTO.setAvailableOnlyOnSite(true);

        // Act
        var result = menuItemMapper.toEntity(createMenuItemDTO);

        // Assert
        assertTrue(result.getAvailableOnlyOnSite());
    }

    @Test
    void toMenuItemResponseDTO_ShouldReturnMenuItemDTO_WhenValidMenuItem() {
        // Act
        var result = menuItemMapper.toMenuItemResponseDTO(menuItem);

        // Assert
        assertNotNull(result);
        assertEquals(menuItem.getNrSeqMenuItem(), result.getId());
        assertEquals(menuItem.getName(), result.getName());
        assertEquals(menuItem.getDescription(), result.getDescription());
        assertEquals(menuItem.getPrice(), result.getPrice());
        assertEquals(menuItem.getImagePath(), result.getImagePath());
        assertEquals(menuItem.getAvailableOnlyOnSite(), result.isAvailableOnlyOnSite());
        assertEquals(menuItem.getRestaurantId(), result.getRestaurantId());
    }

    @Test
    void toMenuItemResponseDTO_ShouldMapAllFields_WhenCalled() {
        // Arrange
        menuItem.setNrSeqMenuItem(5L);
        menuItem.setName("Lasanha Bolonhesa");
        menuItem.setDescription("Lasanha com molho bolonhesa tradicional");
        menuItem.setPrice(28.75);
        menuItem.setAvailableOnlyOnSite(true);
        menuItem.setImagePath("images/lasanha.jpg");
        menuItem.setRestaurantId(3L);

        // Act
        var result = menuItemMapper.toMenuItemResponseDTO(menuItem);

        // Assert
        assertEquals(5L, result.getId());
        assertEquals("Lasanha Bolonhesa", result.getName());
        assertEquals("Lasanha com molho bolonhesa tradicional", result.getDescription());
        assertEquals(28.75, result.getPrice());
        assertTrue(result.isAvailableOnlyOnSite());
        assertEquals("images/lasanha.jpg", result.getImagePath());
        assertEquals(3L, result.getRestaurantId());
    }

    @Test
    void toMenuItemResponseDTO_ShouldHandleNullValues_WhenMenuItemHasNullFields() {
        // Arrange
        menuItem.setName(null);
        menuItem.setDescription(null);
        menuItem.setPrice(null);
        menuItem.setImagePath(null);

        // Act
        var result = menuItemMapper.toMenuItemResponseDTO(menuItem);

        // Assert
        assertNotNull(result);
        assertEquals(menuItem.getNrSeqMenuItem(), result.getId());
        assertNull(result.getName());
        assertNull(result.getDescription());
        assertNull(result.getPrice());
        assertNull(result.getImagePath());
        assertEquals(menuItem.getRestaurantId(), result.getRestaurantId());
    }

    @Test
    void toMenuItemResponseDTO_ShouldHandleZeroPrice_WhenPriceIsZero() {
        // Arrange
        menuItem.setPrice(0.0);

        // Act
        var result = menuItemMapper.toMenuItemResponseDTO(menuItem);

        // Assert
        assertEquals(0.0, result.getPrice());
    }

    @Test
    void toMenuItemResponseDTO_ShouldHandleAvailabilityFlags_WhenDifferentValues() {
        // Arrange & Act & Assert - Test false
        menuItem.setAvailableOnlyOnSite(false);
        var resultFalse = menuItemMapper.toMenuItemResponseDTO(menuItem);
        assertFalse(resultFalse.isAvailableOnlyOnSite());

        // Test true
        menuItem.setAvailableOnlyOnSite(true);
        var resultTrue = menuItemMapper.toMenuItemResponseDTO(menuItem);
        assertTrue(resultTrue.isAvailableOnlyOnSite());
    }

    @Test
    void toResponseListDTO_ShouldReturnEmptyList_WhenEmptyMenuItemList() {
        // Arrange
        List<MenuItem> emptyList = new ArrayList<>();

        // Act
        var result = menuItemMapper.toResponseListDTO(emptyList);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toResponseListDTO_ShouldReturnListOfMenuItemDTO_WhenValidMenuItemList() {
        // Arrange
        MenuItem menuItem2 = new MenuItem();
        menuItem2.setNrSeqMenuItem(2L);
        menuItem2.setName("Salada Caesar");
        menuItem2.setDescription("Salada fresca com molho caesar");
        menuItem2.setPrice(18.50);
        menuItem2.setAvailableOnlyOnSite(false);
        menuItem2.setImagePath("images/caesar-salad.jpg");
        menuItem2.setRestaurantId(1L);

        List<MenuItem> menuItemList = Arrays.asList(menuItem, menuItem2);

        // Act
        var result = menuItemMapper.toResponseListDTO(menuItemList);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(menuItem.getNrSeqMenuItem(), result.get(0).getId());
        assertEquals(menuItem2.getNrSeqMenuItem(), result.get(1).getId());
        assertEquals(menuItem.getName(), result.get(0).getName());
        assertEquals(menuItem2.getName(), result.get(1).getName());
    }

    @Test
    void toResponseListDTO_ShouldMapAllMenuItems_WhenCalled() {
        // Arrange
        MenuItem menuItem1 = new MenuItem();
        menuItem1.setNrSeqMenuItem(10L);
        menuItem1.setName("Risotto de Camarão");
        menuItem1.setDescription("Risotto cremoso com camarões frescos");
        menuItem1.setPrice(45.90);
        menuItem1.setAvailableOnlyOnSite(true);
        menuItem1.setImagePath("images/risotto.jpg");
        menuItem1.setRestaurantId(4L);

        MenuItem menuItem2 = new MenuItem();
        menuItem2.setNrSeqMenuItem(11L);
        menuItem2.setName("Sanduíche Vegano");
        menuItem2.setDescription("Sanduíche com ingredientes 100% vegetais");
        menuItem2.setPrice(22.00);
        menuItem2.setAvailableOnlyOnSite(false);
        menuItem2.setImagePath("images/vegan-sandwich.jpg");
        menuItem2.setRestaurantId(5L);

        List<MenuItem> menuItemList = Arrays.asList(menuItem1, menuItem2);

        // Act
        var result = menuItemMapper.toResponseListDTO(menuItemList);

        // Assert
        assertEquals(2, result.size());

        MenuItemDTO firstResult = result.get(0);
        assertEquals(10L, firstResult.getId());
        assertEquals("Risotto de Camarão", firstResult.getName());
        assertEquals("Risotto cremoso com camarões frescos", firstResult.getDescription());
        assertEquals(45.90, firstResult.getPrice());
        assertTrue(firstResult.isAvailableOnlyOnSite());
        assertEquals("images/risotto.jpg", firstResult.getImagePath());
        assertEquals(4L, firstResult.getRestaurantId());

        MenuItemDTO secondResult = result.get(1);
        assertEquals(11L, secondResult.getId());
        assertEquals("Sanduíche Vegano", secondResult.getName());
        assertEquals("Sanduíche com ingredientes 100% vegetais", secondResult.getDescription());
        assertEquals(22.00, secondResult.getPrice());
        assertFalse(secondResult.isAvailableOnlyOnSite());
        assertEquals("images/vegan-sandwich.jpg", secondResult.getImagePath());
        assertEquals(5L, secondResult.getRestaurantId());
    }

    @Test
    void toResponseListDTO_ShouldHandleNullValuesInList_WhenSomeItemsHaveNullFields() {
        // Arrange
        MenuItem menuItemWithNulls = new MenuItem();
        menuItemWithNulls.setNrSeqMenuItem(3L);
        menuItemWithNulls.setName(null);
        menuItemWithNulls.setDescription(null);
        menuItemWithNulls.setPrice(null);
        menuItemWithNulls.setAvailableOnlyOnSite(false);
        menuItemWithNulls.setImagePath(null);
        menuItemWithNulls.setRestaurantId(2L);

        List<MenuItem> menuItemList = Arrays.asList(menuItem, menuItemWithNulls);

        // Act
        var result = menuItemMapper.toResponseListDTO(menuItemList);

        // Assert
        assertEquals(2, result.size());
        assertNotNull(result.get(0).getName());
        assertNull(result.get(1).getName());
        assertNull(result.get(1).getDescription());
        assertNull(result.get(1).getPrice());
        assertNull(result.get(1).getImagePath());
    }

    @Test
    void toResponseListDTO_ShouldHandleMixedAvailability_WhenItemsHaveDifferentAvailability() {
        // Arrange
        MenuItem availableOnSite = new MenuItem();
        availableOnSite.setNrSeqMenuItem(20L);
        availableOnSite.setName("Prato Executivo");
        availableOnSite.setDescription("Disponível apenas no local");
        availableOnSite.setPrice(15.00);
        availableOnSite.setAvailableOnlyOnSite(true);
        availableOnSite.setRestaurantId(1L);

        MenuItem availableEverywhere = new MenuItem();
        availableEverywhere.setNrSeqMenuItem(21L);
        availableEverywhere.setName("Pizza para Delivery");
        availableEverywhere.setDescription("Disponível para entrega");
        availableEverywhere.setPrice(30.00);
        availableEverywhere.setAvailableOnlyOnSite(false);
        availableEverywhere.setRestaurantId(1L);

        List<MenuItem> menuItemList = Arrays.asList(availableOnSite, availableEverywhere);

        // Act
        var result = menuItemMapper.toResponseListDTO(menuItemList);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.get(0).isAvailableOnlyOnSite());
        assertFalse(result.get(1).isAvailableOnlyOnSite());
    }

    @Test
    void toResponseListDTO_ShouldHandleSingleItem_WhenListHasOneElement() {
        // Arrange
        List<MenuItem> singleItemList = Arrays.asList(menuItem);

        // Act
        var result = menuItemMapper.toResponseListDTO(singleItemList);

        // Assert
        assertEquals(1, result.size());
        assertEquals(menuItem.getNrSeqMenuItem(), result.get(0).getId());
        assertEquals(menuItem.getName(), result.get(0).getName());
    }

    @Test
    void toEntity_ShouldWorkWithHighPrice_WhenPriceIsHigh() {
        // Arrange
        createMenuItemDTO.setPrice(999.99);

        // Act
        var result = menuItemMapper.toEntity(createMenuItemDTO);

        // Assert
        assertEquals(999.99, result.getPrice());
    }

    @Test
    void toMenuItemResponseDTO_ShouldWorkWithHighPrice_WhenPriceIsHigh() {
        // Arrange
        menuItem.setPrice(999.99);

        // Act
        var result = menuItemMapper.toMenuItemResponseDTO(menuItem);

        // Assert
        assertEquals(999.99, result.getPrice());
    }
}
