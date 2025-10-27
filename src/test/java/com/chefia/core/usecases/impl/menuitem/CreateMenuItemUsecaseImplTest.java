package com.chefia.core.usecases.impl.menuitem;

import com.chefia.core.entities.MenuItem;
import com.chefia.core.gateway.MenuItemGateway;
import com.chefia.core.gateway.MenuItemValidatorGateway;
import com.chefia.core.mapper.MenuItemMapper;
import com.chefia.menuitems.model.CreateMenuItemDTO;
import com.chefia.menuitems.model.MenuItemDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateMenuItemUsecaseImplTest {

    @Mock
    private MenuItemGateway menuItemGateway;

    @Mock
    private List<MenuItemValidatorGateway> menuItemValidatorGatewayList;

    @Mock
    private MenuItemMapper menuItemMapper;

    @Mock
    private MenuItemValidatorGateway validator1;

    @Mock
    private MenuItemValidatorGateway validator2;

    @InjectMocks
    private CreateMenuItemUsecaseImpl createMenuItemUsecase;

    private CreateMenuItemDTO createMenuItemDTO;
    private MenuItem menuItem;
    private MenuItemDTO menuItemDTO;

    @BeforeEach
    void setUp() {
        createMenuItemDTO = new CreateMenuItemDTO();
        menuItem = new MenuItem();
        menuItem.setNrSeqMenuItem(1L);
        menuItem.setName("Burger");
        menuItem.setDescription("Delicious burger");
        menuItem.setPrice(15.99);
        menuItem.setAvailableOnlyOnSite(true);
        menuItem.setRestaurantId(1L);

        menuItemDTO = new MenuItemDTO();

        menuItemValidatorGatewayList = Arrays.asList(validator1, validator2);
        createMenuItemUsecase = new CreateMenuItemUsecaseImpl(menuItemGateway, menuItemValidatorGatewayList, menuItemMapper);
    }

    @Test
    void execute_ShouldReturnMenuItemDTO_WhenValidData() {
        // Arrange
        Long savedId = 1L;
        when(menuItemMapper.toEntity(any(CreateMenuItemDTO.class))).thenReturn(menuItem);
        when(menuItemGateway.save(any(MenuItem.class))).thenReturn(savedId);
        when(menuItemMapper.toMenuItemResponseDTO(any(MenuItem.class))).thenReturn(menuItemDTO);

        // Act
        MenuItemDTO result = createMenuItemUsecase.execute(createMenuItemDTO);

        // Assert
        assertNotNull(result);
        assertEquals(menuItemDTO, result);
        verify(menuItemMapper).toEntity(createMenuItemDTO);
        verify(menuItemGateway).save(menuItem);
        verify(menuItemMapper).toMenuItemResponseDTO(menuItem);
    }

    @Test
    void execute_ShouldCallAllValidators_WhenExecuted() {
        // Arrange
        Long savedId = 1L;
        when(menuItemMapper.toEntity(any(CreateMenuItemDTO.class))).thenReturn(menuItem);
        when(menuItemGateway.save(any(MenuItem.class))).thenReturn(savedId);
        when(menuItemMapper.toMenuItemResponseDTO(any(MenuItem.class))).thenReturn(menuItemDTO);

        // Act
        createMenuItemUsecase.execute(createMenuItemDTO);

        // Assert
        verify(validator1).validate(menuItem);
        verify(validator2).validate(menuItem);
    }

    @Test
    void execute_ShouldSetMenuItemId_WhenSaved() {
        // Arrange
        Long savedId = 3L;
        MenuItem menuItemToVerify = new MenuItem();
        menuItemToVerify.setName("Pizza");

        when(menuItemMapper.toEntity(any(CreateMenuItemDTO.class))).thenReturn(menuItemToVerify);
        when(menuItemGateway.save(any(MenuItem.class))).thenReturn(savedId);
        when(menuItemMapper.toMenuItemResponseDTO(any(MenuItem.class))).thenReturn(menuItemDTO);

        // Act
        createMenuItemUsecase.execute(createMenuItemDTO);

        // Assert
        assertEquals(savedId, menuItemToVerify.getNrSeqMenuItem());
        verify(menuItemMapper).toMenuItemResponseDTO(menuItemToVerify);
    }

    @Test
    void execute_ShouldCallMethodsInCorrectOrder_WhenExecuted() {
        // Arrange
        Long savedId = 1L;
        when(menuItemMapper.toEntity(any(CreateMenuItemDTO.class))).thenReturn(menuItem);
        when(menuItemGateway.save(any(MenuItem.class))).thenReturn(savedId);
        when(menuItemMapper.toMenuItemResponseDTO(any(MenuItem.class))).thenReturn(menuItemDTO);

        // Act
        createMenuItemUsecase.execute(createMenuItemDTO);

        // Assert
        var inOrder = inOrder(menuItemMapper, validator1, validator2, menuItemGateway);
        inOrder.verify(menuItemMapper).toEntity(createMenuItemDTO);
        inOrder.verify(validator1).validate(menuItem);
        inOrder.verify(validator2).validate(menuItem);
        inOrder.verify(menuItemGateway).save(menuItem);
        inOrder.verify(menuItemMapper).toMenuItemResponseDTO(menuItem);
    }

    @Test
    void validateMenuItem_ShouldCallAllValidators_WhenMenuItemHasMultipleValidators() {
        // Arrange
        when(menuItemMapper.toEntity(any(CreateMenuItemDTO.class))).thenReturn(menuItem);
        when(menuItemGateway.save(any(MenuItem.class))).thenReturn(1L);
        when(menuItemMapper.toMenuItemResponseDTO(any(MenuItem.class))).thenReturn(menuItemDTO);

        // Act
        createMenuItemUsecase.execute(createMenuItemDTO);

        // Assert
        verify(validator1, times(1)).validate(menuItem);
        verify(validator2, times(1)).validate(menuItem);
    }

    @Test
    void execute_ShouldHandleEmptyValidatorList_WhenNoValidators() {
        // Arrange
        Long savedId = 1L;
        List<MenuItemValidatorGateway> emptyValidatorList = Arrays.asList();
        CreateMenuItemUsecaseImpl usecaseWithNoValidators = new CreateMenuItemUsecaseImpl(
            menuItemGateway, emptyValidatorList, menuItemMapper);

        when(menuItemMapper.toEntity(any(CreateMenuItemDTO.class))).thenReturn(menuItem);
        when(menuItemGateway.save(any(MenuItem.class))).thenReturn(savedId);
        when(menuItemMapper.toMenuItemResponseDTO(any(MenuItem.class))).thenReturn(menuItemDTO);

        // Act
        MenuItemDTO result = usecaseWithNoValidators.execute(createMenuItemDTO);

        // Assert
        assertNotNull(result);
        assertEquals(menuItemDTO, result);
        verify(menuItemMapper).toEntity(createMenuItemDTO);
        verify(menuItemGateway).save(menuItem);
        verify(menuItemMapper).toMenuItemResponseDTO(menuItem);
    }
}
