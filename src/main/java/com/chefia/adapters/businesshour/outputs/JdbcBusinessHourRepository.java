package com.chefia.adapters.businesshour.outputs;

import com.chefia.core.port.output.businesshour.BusinessHourRepositoryOutputPort;
import com.chefia.domain.model.BusinessHours;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;

public class JdbcBusinessHourRepository implements BusinessHourRepositoryOutputPort {

    private final JdbcClient jdbcClient;

    public JdbcBusinessHourRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void save(List<BusinessHours> businessHoursList, Long userRestaurantId) {
        for (BusinessHours businessHoursItem: businessHoursList) {
            jdbcClient.sql("""
                        INSERT INTO chefia.business_hours
                            (week_day, opening_time, closing_time, nr_seq_restaurant)
                        VALUES
                            (:weekDay, :openingTime, :closingTime, :restaurant_id)
                        """)
                    .param("weekDay", businessHoursItem.getWeekDay().name())
                    .param("openingTime", businessHoursItem.getOpeningTime())
                    .param("closingTime", businessHoursItem.getClosingTime())
                    .param("restaurant_id", userRestaurantId)
                    .update();
        }
    }

    @Override
    public List<BusinessHours> findById(Long restaurantId) {
        return jdbcClient.sql("""
                        SELECT * FROM chefia.business_hours
                        WHERE nr_seq_restaurant = :id
                        """)
                .param("id", restaurantId)
                .query(BusinessHours.class)
                .list();
    }

    @Override
    public void deleteByRestaurantIdBusinessHours(Long restaurantId){
        jdbcClient.sql("""
                        DELETE FROM chefia.business_hours
                        WHERE nr_seq_restaurant = :id
                        """)
                .param("id", restaurantId)
                .update();
    }
}

