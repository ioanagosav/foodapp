package ro.foodApp.services;

import com.google.inject.Inject;
import ro.foodApp.dao.OrderDAO;
import ro.foodApp.dao.RestaurantDAO;
import ro.foodApp.entities.Order;
import ro.foodApp.entities.Restaurant;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Path("/restaurant")
public class RestaurantDataService {

    private static final Logger LOG = Logger.getLogger(GroupDataService.class.getName());

    private final OrderDAO orderDao;
    private final RestaurantDAO restaurantDao;

    @Inject
    private RestaurantDataService(OrderDAO orderDao, RestaurantDAO restaurantDao) {
        this.orderDao = orderDao;
        this.restaurantDao = restaurantDao;
    }

    @POST
    @Path("/getPreferredRestaurants")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Set<Restaurant> getExistentRestaurantsForGroup(Long groupId) {
        Set<Restaurant> restaurants = new HashSet<>();
        List<Order> orders = orderDao.getAllOrdersForGroup(groupId);

        for (Order order : orders) {
            Restaurant restaurant = restaurantDao.getRestaurantById(order.getRestaurantId());
            if (null != restaurant) {
                restaurants.add(restaurant);
            }
        }

        return restaurants;
    }

    @POST
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Long saveRestaurant(Restaurant restaurant) {
        return restaurantDao.saveRestaurant(restaurant);
    }

    @POST
    @Path("/getRestaurantById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Restaurant getRestaurantById(Long restaurantId) {
        return restaurantDao.getRestaurantById(restaurantId);
    }
}
