package com.chefia.infra.database.jdbc.repository;

import com.chefia.core.entities.BusinessHours;
import com.chefia.restaurants.model.CreateBusinessHoursDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JdbcBusinessHourRepositoryTest {

    @Mock
    private JdbcClient jdbcClient;

    @Mock
    private JdbcClient.StatementSpec statementSpec;

    @Mock
    private JdbcClient.MappedQuerySpec<BusinessHours> mappedQuerySpec;

    @InjectMocks
    private JdbcBusinessHourRepository businessHourRepository;

    private BusinessHours businessHours;
    private List<BusinessHours> businessHoursList;

    @BeforeEach
    void setUp() {
        businessHours = new BusinessHours(
            CreateBusinessHoursDTO.WeekDayEnum.MONDAY,
            LocalDateTime.of(2023, 1, 1, 9, 0),
            LocalDateTime.of(2023, 1, 1, 18, 0)
        );

        BusinessHours businessHours2 = new BusinessHours(
            CreateBusinessHoursDTO.WeekDayEnum.TUESDAY,
            LocalDateTime.of(2023, 1, 1, 10, 0),
            LocalDateTime.of(2023, 1, 1, 19, 0)
        );

        businessHoursList = Arrays.asList(businessHours, businessHours2);
    }

    @Test
    void save_ShouldCallUpdateForEachBusinessHour_WhenSavingList() {
        // Arrange
        var restaurantId = 1L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.update()).thenReturn(1);

        // Act
        businessHourRepository.save(businessHoursList, restaurantId);

        // Assert
        verify(jdbcClient, times(2)).sql(anyString());
        verify(statementSpec, times(8)).param(anyString(), any());
        verify(statementSpec, times(2)).update();
    }

    @Test
    void save_ShouldCallCorrectParameters_WhenSavingBusinessHours() {
        // Arrange
        var restaurantId = 5L;
        var singleBusinessHourList = Arrays.asList(businessHours);
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.update()).thenReturn(1);

        // Act
        businessHourRepository.save(singleBusinessHourList, restaurantId);

        // Assert
        verify(statementSpec).param("weekDay", businessHours.getWeekDay().name());
        verify(statementSpec).param("openingTime", businessHours.getOpeningTime());
        verify(statementSpec).param("closingTime", businessHours.getClosingTime());
        verify(statementSpec).param("restaurant_id", restaurantId);
    }

    @Test
    void save_ShouldHandleEmptyList_WhenListIsEmpty() {
        // Arrange
        var restaurantId = 1L;
        var emptyList = Arrays.<BusinessHours>asList();

        // Act
        businessHourRepository.save(emptyList, restaurantId);

        // Assert
        verify(jdbcClient, never()).sql(anyString());
        verify(statementSpec, never()).param(anyString(), any());
        verify(statementSpec, never()).update();
    }

    @Test
    void findById_ShouldReturnBusinessHoursList_WhenBusinessHoursExist() {
        // Arrange
        var restaurantId = 1L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(BusinessHours.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.list()).thenReturn(businessHoursList);

        // Act
        var result = businessHourRepository.findById(restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(businessHoursList, result);
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("id", restaurantId);
        verify(mappedQuerySpec).list();
    }

    @Test
    void findById_ShouldReturnEmptyList_WhenNoBusinessHoursExist() {
        // Arrange
        var restaurantId = 999L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(BusinessHours.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.list()).thenReturn(Arrays.asList());

        // Act
        var result = businessHourRepository.findById(restaurantId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("id", restaurantId);
        verify(mappedQuerySpec).list();
    }

    @Test
    void deleteByRestaurantIdBusinessHours_ShouldCallDeleteWithCorrectParameter_WhenDeletingBusinessHours() {
        // Arrange
        var restaurantId = 1L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.update()).thenReturn(1);

        // Act
        businessHourRepository.deleteByRestaurantIdBusinessHours(restaurantId);

        // Assert
        verify(jdbcClient).sql(anyString());
        verify(statementSpec).param("id", restaurantId);
        verify(statementSpec).update();
    }

    @Test
    void deleteByRestaurantIdBusinessHours_ShouldCallUpdateOnce_WhenDeletingBusinessHours() {
        // Arrange
        var restaurantId = 3L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.update()).thenReturn(1);

        // Act
        businessHourRepository.deleteByRestaurantIdBusinessHours(restaurantId);

        // Assert
        verify(jdbcClient, times(1)).sql(anyString());
        verify(statementSpec, times(1)).param("id", restaurantId);
        verify(statementSpec, times(1)).update();
    }

    @Test
    void save_ShouldHandleDifferentWeekDays_WhenSavingBusinessHours() {
        // Arrange
        var restaurantId = 2L;
        var weekendBusinessHours = new BusinessHours(
            CreateBusinessHoursDTO.WeekDayEnum.SATURDAY,
            LocalDateTime.of(2023, 1, 1, 11, 0),
            LocalDateTime.of(2023, 1, 1, 22, 0)
        );

        var weekendList = Arrays.asList(weekendBusinessHours);
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.update()).thenReturn(1);

        // Act
        businessHourRepository.save(weekendList, restaurantId);

        // Assert
        verify(statementSpec).param("weekDay", "SATURDAY");
        verify(statementSpec).param("openingTime", LocalDateTime.of(2023, 1, 1, 11, 0));
        verify(statementSpec).param("closingTime", LocalDateTime.of(2023, 1, 1, 22, 0));
        verify(statementSpec).param("restaurant_id", restaurantId);
    }

    @Test
    void findById_ShouldCallGatewayWithCorrectRestaurantId_WhenSearching() {
        // Arrange
        var specificRestaurantId = 42L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.query(BusinessHours.class)).thenReturn(mappedQuerySpec);
        when(mappedQuerySpec.list()).thenReturn(businessHoursList);

        // Act
        businessHourRepository.findById(specificRestaurantId);

        // Assert
        verify(statementSpec).param("id", specificRestaurantId);
    }

    @Test
    void save_ShouldProcessAllItemsInList_WhenSavingMultipleBusinessHours() {
        // Arrange
        var restaurantId = 1L;
        when(jdbcClient.sql(anyString())).thenReturn(statementSpec);
        when(statementSpec.param(anyString(), any())).thenReturn(statementSpec);
        when(statementSpec.update()).thenReturn(1);

        // Act
        businessHourRepository.save(businessHoursList, restaurantId);

        // Assert
        verify(jdbcClient, times(businessHoursList.size())).sql(anyString());
        verify(statementSpec, times(businessHoursList.size() * 4)).param(anyString(), any());
        verify(statementSpec, times(businessHoursList.size())).update();
    }
}
