package com.chefia.core.usecases.impl.restaurant;

import com.chefia.core.entities.Restaurant;
import com.chefia.core.gateway.RestaurantGateway;
import com.chefia.core.mapper.RestaurantMapper;
import com.chefia.core.usecases.interfaces.restaurant.ReadAllRestauranteUsecase;
import com.chefia.restaurants.model.PaginatedRestaurantsDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class ReadAllRestaurantUsecaseImpl implements ReadAllRestauranteUsecase {


    private final RestaurantGateway restaurantGateway;
    private final RestaurantMapper restaurantMapper;

    @Override
    public PaginatedRestaurantsDTO execute(Integer page, Integer perPage) {
        Pageable pageable = PageRequest.of(page, perPage);
        List<Restaurant> restaurantPage = this.restaurantGateway.findAll(pageable);

        var restaurantsDTO = this.restaurantMapper.toResponseListDTO(restaurantPage);

        return new PaginatedRestaurantsDTO()
                .page(page)
                .perPage(perPage)
                .total((long) restaurantPage.size())
                .items(restaurantsDTO);
    }
}
