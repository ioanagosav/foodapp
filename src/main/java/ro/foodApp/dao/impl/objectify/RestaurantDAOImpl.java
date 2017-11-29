package ro.foodApp.dao.impl.objectify;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.Filter;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import ro.foodApp.dao.RestaurantDAO;
import ro.foodApp.entities.Restaurant;

import java.util.List;

public class RestaurantDAOImpl implements RestaurantDAO {

    public Long saveRestaurant(Restaurant restaurant) {
        Object obj = ObjectifyService.ofy().save().entity(restaurant).now();
        return ((Key) obj).getId();
    }

    public Restaurant getRestaurantByName(String restaurantName) {
        Filter restaurantNameFilter = new FilterPredicate("name", FilterOperator.EQUAL, restaurantName);
        List<Restaurant> restaurantList = ObjectifyService.ofy().load().type(Restaurant.class).filter(restaurantNameFilter).list();
        return restaurantList.get(0);
    }

    @Override
    public Restaurant getRestaurantById(Long restaurantId) {
        return ObjectifyService.ofy().load()
                .type(Restaurant.class)
                .filterKey(Key.create(Restaurant.class, restaurantId))
                .first()
                .now();
    }

}
