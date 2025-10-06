package com.chefia.infra.web;

import com.chefia.core.controllers.RestaurantController;
import com.chefia.restaurants.api.RestaurantApi;
import com.chefia.restaurants.model.CreateRestaurantDTO;
import com.chefia.restaurants.model.PaginatedRestaurantsDTO;
import com.chefia.restaurants.model.RestaurantDTO;
import com.chefia.restaurants.model.UpdateRestaurantDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class RestaurantApiController implements RestaurantApi {

    private final RestaurantController restaurantController;

    @Override
    public ResponseEntity<RestaurantDTO> createRestaurant(CreateRestaurantDTO body)
    {
        log.info("[POST] - Create Restaurant");
        var createdUser = this.restaurantController.createRestaurant(body);
        return ResponseEntity.status(201).body(createdUser);
    }

    @Override
    public ResponseEntity<RestaurantDTO> getRestaurant(Long restaurantId)
    {
        log.info("[GET] - List Restaurant");
        var getUser = this.restaurantController.getRestaurant(restaurantId);
        return ResponseEntity.ok(getUser);
    }

    @Override
    public ResponseEntity<PaginatedRestaurantsDTO> listRestaurants(Integer page, Integer perPage)
    {
        log.info("[GET] - List All Restaurant");
        var listAllUsers = this.restaurantController.listRestaurants(page, perPage);
        return ResponseEntity.ok(listAllUsers);
    }

    @Override
    public ResponseEntity<RestaurantDTO> updateRestaurant(Long restaurantId, UpdateRestaurantDTO body)
    {
        log.info("[PUT] - Update Restaurant");
        var updatedUser = this.restaurantController.updateRestaurant(restaurantId, body);
        return ResponseEntity.ok().body(updatedUser);
    }

    @Override
    public ResponseEntity<Void> deleteRestaurant(Long restaurantId)
    {
        log.info("[DELETE] - Remove Restaurant");
        this.restaurantController.deleteRestaurant(restaurantId);
        return ResponseEntity.noContent().build();
    }
}
