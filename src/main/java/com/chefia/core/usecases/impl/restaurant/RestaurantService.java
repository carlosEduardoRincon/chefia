package com.chefia.core.usecases.impl.restaurant;

import com.chefia.core.usecases.interfaces.restaurant.RestaurantInputPort;
import com.chefia.core.gateway.BusinessHourGateway;
import com.chefia.core.gateway.RestaurantGateway;
import com.chefia.core.entities.Restaurant;
import com.chefia.core.exceptions.RestaurantNotFoundException;
import com.chefia.core.exceptions.UserNotFoundException;
import com.chefia.core.mapper.BusinessHoursMapper;
import com.chefia.core.mapper.RestaurantMapper;
import com.chefia.restaurants.model.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService implements RestaurantInputPort {

    private final RestaurantGateway restaurantGateway;
    private final BusinessHourGateway businessHourGateway;
    private final RestaurantMapper restaurantMapper;
    private final BusinessHoursMapper businessHoursMapper;

    public RestaurantService(RestaurantGateway restaurantGateway,
                             BusinessHourGateway businessHourGateway,
                             RestaurantMapper restaurantMapper,
                             BusinessHoursMapper businessHoursMapper
    ) {
        this.restaurantGateway = restaurantGateway;
        this.businessHourGateway = businessHourGateway;
        this.restaurantMapper = restaurantMapper;
        this.businessHoursMapper = businessHoursMapper;
    }

    @Override
    public RestaurantDTO saveRestaurant(CreateRestaurantDTO createRestaurantDTO) {
        var restaurantToInsert = this.restaurantMapper.toEntity(createRestaurantDTO);

        var restaurantId = this.restaurantGateway.save(restaurantToInsert);
        restaurantToInsert.setNrSeqRestaurant(restaurantId);

        var newBusinessHourslist = this.businessHoursMapper.toEntity(createRestaurantDTO.getBusinessHours());
        this.businessHourGateway.save(newBusinessHourslist, restaurantId);

        return this.restaurantMapper.toRestaurantResponseDTO(restaurantToInsert);
    }

    @Override
    public RestaurantDTO findById(Long restaurantId) {
        var restaurant = Optional.ofNullable(this.restaurantGateway
                .findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant Item not found with id: " + restaurantId)));
        assert restaurant.isPresent();

        var businessHours = this.businessHourGateway.findById(restaurantId);
        restaurant.get().setBusinessHours(businessHours);
        return this.restaurantMapper.toRestaurantResponseDTO(restaurant.get());
    }

    @Override
    public PaginatedRestaurantsDTO findAll(Integer page, Integer perPage) {
        Pageable pageable = PageRequest.of(page, perPage);
        List<Restaurant> restaurantPage = this.restaurantGateway.findAll(pageable);

        var restaurantsDTO = this.restaurantMapper.toResponseListDTO(restaurantPage);

        return new PaginatedRestaurantsDTO()
                .page(page)
                .perPage(perPage)
                .total((long) restaurantPage.size())
                .items(restaurantsDTO);
    }

    @Override
    public RestaurantDTO updateRestaurant(Long restaurantId, UpdateRestaurantDTO updateRestaurantDTO) {
        var restaurantEntity = this.restaurantGateway
                .findById(restaurantId)
                .orElseThrow(() -> new UserNotFoundException("Restaurant not found with id: " + restaurantId));

        restaurantEntity.setName(updateRestaurantDTO.getName());
        restaurantEntity.setRestaurantType(RestaurantDTO.RestaurantTypeEnum.fromValue(updateRestaurantDTO.getRestaurantType().name()));
        restaurantEntity.setActive(updateRestaurantDTO.isActive());
        restaurantEntity.setBusinessHours(this.businessHoursMapper.toEntity(updateRestaurantDTO.getBusinessHours()));

        if (!updateRestaurantDTO.getBusinessHours().isEmpty()) {
            this.businessHourGateway.deleteByRestaurantIdBusinessHours(restaurantId);
            var newBusinessHourslist = this.businessHoursMapper.toEntity(updateRestaurantDTO.getBusinessHours());
            this.businessHourGateway.save(newBusinessHourslist, restaurantId);
        }

        this.restaurantGateway.update(restaurantId, restaurantEntity);

        return this.restaurantMapper.toRestaurantResponseDTO(restaurantEntity);
    }

    @Override
    public void deleteRestaurant(Long restaurantId) {
        this.businessHourGateway.deleteByRestaurantIdBusinessHours(restaurantId);
        this.restaurantGateway.deleteById(restaurantId);
    }
}
