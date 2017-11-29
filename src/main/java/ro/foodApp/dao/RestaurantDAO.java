package ro.foodApp.dao;

import ro.foodApp.entities.Restaurant;

public interface RestaurantDAO {

    Long saveRestaurant(Restaurant restaurant);

    Restaurant getRestaurantByName(String restaurantName);

    Restaurant getRestaurantById(Long restaurantDao);

}
