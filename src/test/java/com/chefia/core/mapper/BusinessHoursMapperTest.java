package com.chefia.core.mapper;

import com.chefia.core.entities.BusinessHours;
import com.chefia.restaurants.model.CreateBusinessHoursDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BusinessHoursMapperTest {

    @InjectMocks
    private BusinessHoursMapper businessHoursMapper;

    private CreateBusinessHoursDTO createBusinessHoursDTO;
    private BusinessHours businessHours;
    private List<CreateBusinessHoursDTO> businessHoursDTOList;
    private List<BusinessHours> businessHoursList;

    @BeforeEach
    void setUp() {
        createBusinessHoursDTO = new CreateBusinessHoursDTO();
        createBusinessHoursDTO.setWeekDay(CreateBusinessHoursDTO.WeekDayEnum.MONDAY);
        createBusinessHoursDTO.setOpeningTime(OffsetDateTime.of(2023, 1, 1, 9, 0, 0, 0, ZoneOffset.ofHours(-3)));
        createBusinessHoursDTO.setClosingTime(OffsetDateTime.of(2023, 1, 1, 18, 0, 0, 0, ZoneOffset.ofHours(-3)));

        businessHours = new BusinessHours(CreateBusinessHoursDTO.WeekDayEnum.MONDAY,
                LocalDateTime.of(2023, 1, 1, 9, 0, 0),
                LocalDateTime.of(2023, 1, 1, 18, 0, 0)
                );

        businessHoursDTOList = new ArrayList<>();
        businessHoursList = new ArrayList<>();
    }

    @Test
    void toEntity_ShouldReturnEmptyList_WhenEmptyDTOList() {
        // Arrange
        List<CreateBusinessHoursDTO> emptyList = new ArrayList<>();

        // Act
        var result = businessHoursMapper.toEntity(emptyList);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toEntity_ShouldReturnBusinessHoursList_WhenValidDTOList() {
        // Arrange
        businessHoursDTOList.add(createBusinessHoursDTO);

        // Act
        var result = businessHoursMapper.toEntity(businessHoursDTOList);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(LocalDateTime.of(2023, 1, 1, 9, 0, 0), result.get(0).getOpeningTime());
        assertEquals(LocalDateTime.of(2023, 1, 1, 18, 0, 0), result.get(0).getClosingTime());
    }

    @Test
    void toEntity_ShouldConvertOffsetDateTimeToLocalDateTime_WhenCalled() {
        // Arrange
        OffsetDateTime openingTime = OffsetDateTime.of(2023, 5, 15, 8, 30, 0, 0, ZoneOffset.ofHours(-3));
        OffsetDateTime closingTime = OffsetDateTime.of(2023, 5, 15, 17, 30, 0, 0, ZoneOffset.ofHours(-3));

        createBusinessHoursDTO.setOpeningTime(openingTime);
        createBusinessHoursDTO.setClosingTime(closingTime);
        businessHoursDTOList.add(createBusinessHoursDTO);

        // Act
        var result = businessHoursMapper.toEntity(businessHoursDTOList);

        // Assert
        assertEquals(openingTime.toLocalDateTime(), result.get(0).getOpeningTime());
        assertEquals(closingTime.toLocalDateTime(), result.get(0).getClosingTime());
    }

    @Test
    void toEntity_ShouldMapAllWeekDays_WhenCalled() {
        // Arrange
        CreateBusinessHoursDTO mondayDTO = new CreateBusinessHoursDTO();
        mondayDTO.setWeekDay(CreateBusinessHoursDTO.WeekDayEnum.MONDAY);
        mondayDTO.setOpeningTime(OffsetDateTime.of(2023, 1, 1, 9, 0, 0, 0, ZoneOffset.ofHours(-3)));
        mondayDTO.setClosingTime(OffsetDateTime.of(2023, 1, 1, 17, 0, 0, 0, ZoneOffset.ofHours(-3)));

        CreateBusinessHoursDTO tuesdayDTO = new CreateBusinessHoursDTO();
        tuesdayDTO.setWeekDay(CreateBusinessHoursDTO.WeekDayEnum.TUESDAY);
        tuesdayDTO.setOpeningTime(OffsetDateTime.of(2023, 1, 1, 8, 0, 0, 0, ZoneOffset.ofHours(-3)));
        tuesdayDTO.setClosingTime(OffsetDateTime.of(2023, 1, 1, 18, 0, 0, 0, ZoneOffset.ofHours(-3)));

        businessHoursDTOList.addAll(Arrays.asList(mondayDTO, tuesdayDTO));

        // Act
        var result = businessHoursMapper.toEntity(businessHoursDTOList);

        // Assert
        assertEquals(2, result.size());
        assertEquals(CreateBusinessHoursDTO.WeekDayEnum.MONDAY, result.get(0).getWeekDay());
        assertEquals(CreateBusinessHoursDTO.WeekDayEnum.TUESDAY, result.get(1).getWeekDay());
    }

    @Test
    void toBusinessHoursResponseDTO_ShouldReturnEmptyList_WhenEmptyBusinessHoursList() {
        // Arrange
        List<BusinessHours> emptyList = new ArrayList<>();

        // Act
        var result = businessHoursMapper.toBusinessHoursResponseDTO(emptyList);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toBusinessHoursResponseDTO_ShouldReturnDTOList_WhenValidBusinessHoursList() {
        // Arrange
        businessHoursList.add(businessHours);

        // Act
        var result = businessHoursMapper.toBusinessHoursResponseDTO(businessHoursList);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(CreateBusinessHoursDTO.WeekDayEnum.MONDAY, result.get(0).getWeekDay());
    }

    @Test
    void toEntity_ShouldWorkWithDifferentTimezones_WhenCalled() {
        // Arrange
        OffsetDateTime openingTimeUTC = OffsetDateTime.of(2023, 1, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime closingTimeUTC = OffsetDateTime.of(2023, 1, 1, 21, 0, 0, 0, ZoneOffset.UTC);

        createBusinessHoursDTO.setOpeningTime(openingTimeUTC);
        createBusinessHoursDTO.setClosingTime(closingTimeUTC);
        businessHoursDTOList.add(createBusinessHoursDTO);

        // Act
        var result = businessHoursMapper.toEntity(businessHoursDTOList);

        // Assert
        assertEquals(openingTimeUTC.toLocalDateTime(), result.get(0).getOpeningTime());
        assertEquals(closingTimeUTC.toLocalDateTime(), result.get(0).getClosingTime());
    }

    @Test
    void mappers_ShouldBeConsistent_WhenConvertingBackAndForth() {
        // Arrange
        businessHoursList.add(businessHours);

        // Act
        var dtoResult = businessHoursMapper.toBusinessHoursResponseDTO(businessHoursList);
        var entityResult = businessHoursMapper.toEntity(dtoResult);

        // Assert
        assertEquals(1, entityResult.size());
        assertEquals(businessHours.getWeekDay(), entityResult.get(0).getWeekDay());
        assertEquals(businessHours.getOpeningTime(), entityResult.get(0).getOpeningTime());
        assertEquals(businessHours.getClosingTime(), entityResult.get(0).getClosingTime());
    }

}
