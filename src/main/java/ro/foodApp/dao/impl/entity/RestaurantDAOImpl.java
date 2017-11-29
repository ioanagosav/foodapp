package ro.foodApp.dao.impl.entity;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.Filter;
import ro.foodApp.dao.RestaurantDAO;
import ro.foodApp.entities.Restaurant;

import java.util.List;


public class RestaurantDAOImpl implements RestaurantDAO {

    DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();


    @Override
    public Long saveRestaurant(Restaurant restaurant) {
        Entity restaurantEntity = new Entity("Restaurant");
        restaurantEntity.setProperty("name", restaurant.getName());
        restaurantEntity.setProperty("link", restaurant.getLink());
        datastoreService.put(restaurantEntity);
        return null;
    }

    @Override
    public Restaurant getRestaurantByName(String restaurantName) {
        Filter restaurantNameFilter = new FilterPredicate("name", FilterOperator.EQUAL, restaurantName);
        Query restaurantNameQuery = new Query("Restaurant").setFilter(restaurantNameFilter);
        List<Entity> foundRestaurantEntities = datastoreService.prepare(restaurantNameQuery).asList(FetchOptions.Builder.withDefaults());
        if (foundRestaurantEntities == null || foundRestaurantEntities.size() == 0) {
            return null;
        }
        Restaurant foundRestaurant = new Restaurant();
        foundRestaurant.setId((Long) foundRestaurantEntities.get(0).getProperty("id"));
        foundRestaurant.setName((String) foundRestaurantEntities.get(0).getProperty("name"));
        foundRestaurant.setName((String) foundRestaurantEntities.get(0).getProperty("link"));
        return foundRestaurant;
    }

    @Override
    public Restaurant getRestaurantById(Long restaurantDao) {
        return null;
    }


}
