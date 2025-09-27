package com.chefia.core.service;

import com.chefia.core.port.input.RestaurantInputPort;
import com.chefia.core.port.output.RestaurantRepositoryOutputPort;
import com.chefia.domain.model.Restaurant;
import com.chefia.infra.exception.RestaurantNotFoundException;
import com.chefia.infra.exception.UserNotFoundException;
import com.chefia.infra.mapper.RestaurantMapper;
import com.chefia.restaurants.model.CreateRestaurantDTO;
import com.chefia.restaurants.model.PaginatedRestaurantsDTO;
import com.chefia.restaurants.model.RestaurantDTO;
import com.chefia.restaurants.model.UpdateRestaurantDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RestaurantService implements RestaurantInputPort {

    private final RestaurantRepositoryOutputPort restaurantRepositoryOutputPort;
    private final RestaurantMapper restaurantMapper;

    public RestaurantService(RestaurantRepositoryOutputPort restaurantRepositoryOutputPort,
                             RestaurantMapper restaurantMapper
    ) {
        this.restaurantRepositoryOutputPort = restaurantRepositoryOutputPort;
        this.restaurantMapper = restaurantMapper;
    }

    @Override
    public RestaurantDTO saveRestaurant(CreateRestaurantDTO createRestaurantDTO) {
        var restaurantToInsert = this.restaurantMapper.toEntity(createRestaurantDTO);
        this.restaurantRepositoryOutputPort.save(restaurantToInsert);
        return this.restaurantMapper.toRestaurantResponseDTO(restaurantToInsert);
    }

    @Override
    public RestaurantDTO findById(Long restaurantId) {
        var restaurant = Optional.ofNullable(this.restaurantRepositoryOutputPort
                .findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant Item not found with id: " + restaurantId)));
        assert restaurant.isPresent();
        return this.restaurantMapper.toRestaurantResponseDTO(restaurant.get());
    }

    @Override
    public PaginatedRestaurantsDTO findAll(Integer page, Integer perPage) {
        Pageable pageable = PageRequest.of(page, perPage);
        Page<Restaurant> restaurantPage = this.restaurantRepositoryOutputPort.findAll(pageable);

        var restaurantsDTO = this.restaurantMapper.toResponseListDTO(restaurantPage.getContent());

        return new PaginatedRestaurantsDTO()
                .page(page)
                .perPage(perPage)
                .total(restaurantPage.getTotalElements())
                .items(restaurantsDTO);
    }

    @Override
    public RestaurantDTO updateRestaurant(Long restaurantId, UpdateRestaurantDTO updateRestaurantDTO) {
        var restaurantEntity = this.restaurantRepositoryOutputPort
                .findById(restaurantId)
                .orElseThrow(() -> new UserNotFoundException("Restaurant not found with id: " + restaurantId));

        restaurantEntity.setName(updateRestaurantDTO.getName());
        //restaurantEntity.setBusinessHours(updateRestaurantDTO.getBusinessHours());

        this.restaurantRepositoryOutputPort.update(restaurantId, restaurantEntity);

        return this.restaurantMapper.toRestaurantResponseDTO(restaurantEntity);
    }

    @Override
    public void deleteRestaurant(Long restaurantId) {
        this.restaurantRepositoryOutputPort.deleteById(restaurantId);
    }
}
