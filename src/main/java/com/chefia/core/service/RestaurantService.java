package com.chefia.core.service;

import com.chefia.core.port.input.restaurant.RestaurantInputPort;
import com.chefia.core.port.output.businesshour.BusinessHourRepositoryOutputPort;
import com.chefia.core.port.output.restaurant.RestaurantRepositoryOutputPort;
import com.chefia.domain.model.BusinessHours;
import com.chefia.domain.model.Restaurant;
import com.chefia.infra.exception.RestaurantNotFoundException;
import com.chefia.infra.exception.UserNotFoundException;
import com.chefia.infra.mapper.BusinessHoursMapper;
import com.chefia.infra.mapper.RestaurantMapper;
import com.chefia.restaurants.model.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService implements RestaurantInputPort {

    private final RestaurantRepositoryOutputPort restaurantRepositoryOutputPort;
    private final BusinessHourRepositoryOutputPort businessHourRepositoryOutputPort;
    private final RestaurantMapper restaurantMapper;
    private final BusinessHoursMapper businessHoursMapper;

    public RestaurantService(RestaurantRepositoryOutputPort restaurantRepositoryOutputPort,
                             BusinessHourRepositoryOutputPort businessHourRepositoryOutputPort,
                             RestaurantMapper restaurantMapper,
                             BusinessHoursMapper businessHoursMapper
    ) {
        this.restaurantRepositoryOutputPort = restaurantRepositoryOutputPort;
        this.businessHourRepositoryOutputPort = businessHourRepositoryOutputPort;
        this.restaurantMapper = restaurantMapper;
        this.businessHoursMapper = businessHoursMapper;
    }

    @Override
    public RestaurantDTO saveRestaurant(CreateRestaurantDTO createRestaurantDTO) {
        var restaurantToInsert = this.restaurantMapper.toEntity(createRestaurantDTO);

        var restaurantId = this.restaurantRepositoryOutputPort.save(restaurantToInsert);
        restaurantToInsert.setNrSeqRestaurant(restaurantId);

        var newBusinessHourslist = this.businessHoursMapper.toEntity(createRestaurantDTO.getBusinessHours());
        this.businessHourRepositoryOutputPort.save(newBusinessHourslist, restaurantId);

        return this.restaurantMapper.toRestaurantResponseDTO(restaurantToInsert);
    }

    @Override
    public RestaurantDTO findById(Long restaurantId) {
        var restaurant = Optional.ofNullable(this.restaurantRepositoryOutputPort
                .findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant Item not found with id: " + restaurantId)));
        assert restaurant.isPresent();

        var businessHours = this.businessHourRepositoryOutputPort.findById(restaurantId);
        restaurant.get().setBusinessHours(businessHours);
        return this.restaurantMapper.toRestaurantResponseDTO(restaurant.get());
    }

    @Override
    public PaginatedRestaurantsDTO findAll(Integer page, Integer perPage) {
        Pageable pageable = PageRequest.of(page, perPage);
        List<Restaurant> restaurantPage = this.restaurantRepositoryOutputPort.findAll(pageable);

        var restaurantsDTO = this.restaurantMapper.toResponseListDTO(restaurantPage);

        return new PaginatedRestaurantsDTO()
                .page(page)
                .perPage(perPage)
                .total((long) restaurantPage.size())
                .items(restaurantsDTO);
    }

    @Override
    public RestaurantDTO updateRestaurant(Long restaurantId, UpdateRestaurantDTO updateRestaurantDTO) {
        var restaurantEntity = this.restaurantRepositoryOutputPort
                .findById(restaurantId)
                .orElseThrow(() -> new UserNotFoundException("Restaurant not found with id: " + restaurantId));

        restaurantEntity.setName(updateRestaurantDTO.getName());
        restaurantEntity.setRestaurantType(RestaurantDTO.RestaurantTypeEnum.fromValue(updateRestaurantDTO.getRestaurantType().name()));
        restaurantEntity.setActive(updateRestaurantDTO.isActive());
        restaurantEntity.setBusinessHours(this.businessHoursMapper.toEntity(updateRestaurantDTO.getBusinessHours()));

        if (!updateRestaurantDTO.getBusinessHours().isEmpty()) {
            this.businessHourRepositoryOutputPort.deleteByRestaurantIdBusinessHours(restaurantId);
            var newBusinessHourslist = this.businessHoursMapper.toEntity(updateRestaurantDTO.getBusinessHours());
            this.businessHourRepositoryOutputPort.save(newBusinessHourslist, restaurantId);
        }

        this.restaurantRepositoryOutputPort.update(restaurantId, restaurantEntity);

        return this.restaurantMapper.toRestaurantResponseDTO(restaurantEntity);
    }

    @Override
    public void deleteRestaurant(Long restaurantId) {
        this.businessHourRepositoryOutputPort.deleteByRestaurantIdBusinessHours(restaurantId);
        this.restaurantRepositoryOutputPort.deleteById(restaurantId);
    }
}
