package ro.foodApp.services;

import com.google.inject.Inject;
import org.joda.time.DateTime;
import ro.foodApp.dao.GroupDAO;
import ro.foodApp.dao.OrderDAO;
import ro.foodApp.dao.RestaurantDAO;
import ro.foodApp.entities.*;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/test/order")
public class OrderTestController {

    private final GroupDAO groupDao;
    private final RestaurantDAO restaurantDao;
    private final OrderDAO orderDao;

    private Long recentOrderId;
    private Long recentGroupId;

    @Inject
    public OrderTestController(GroupDAO groupDao, RestaurantDAO restaurantDao, OrderDAO orderDao) {
        this.groupDao = groupDao;
        this.restaurantDao = restaurantDao;
        this.orderDao = orderDao;
    }

    @POST
    @Path("/orderTest")
    @Produces(MediaType.APPLICATION_JSON)
    public Order orderTest() throws Exception {
        Group group = createTestGroup("testingGroup1", 1, 2);
        Restaurant restaurant = createRestaurant("myRestaurant1");
        Product product = createProduct("myProduct1");
        Order order;

        groupDao.saveGroup(group);
        restaurantDao.saveRestaurant(restaurant);
        recentGroupId = group.getId();

        order = createOrder(group.getId(), restaurant.getId(), product);
        orderDao.saveOrder(order);
        recentOrderId = order.getId();


        return order;

    }

    @POST
    @Path("/showOrder")
    @Produces(MediaType.APPLICATION_JSON)
    public Order showCreatedOrder() {
        Order order = orderDao.getOrderById(recentOrderId);
        return order;
    }

    @POST
    @Path("/sendOrder")
    @Produces(MediaType.APPLICATION_JSON)
    public Order sendOrder() {
        Group group = groupDao.getGroupByName("testingGroup1");
        Order order = orderDao.getActiveOrderForGroup(group.getId());
        orderDao.sendOrder(order.getId());
        return order;
    }

    @POST
    @Path("/showActiveOrder")
    @Produces(MediaType.APPLICATION_JSON)
    public Order showActiveOrder() {
        Group group = groupDao.getGroupByName("testingGroup1");
        Order order = orderDao.getActiveOrderForGroup(group.getId());
        return order;
    }

    @POST
    @Path("/showGroup")
    @Produces(MediaType.APPLICATION_JSON)
    public Group showGroup() {
        Group group = groupDao.getGroupByName("testingGroup1");
        return group;
    }


    private Group createTestGroup(String groupName, int numberOfAdmins, int numberOfMembers) {
        Group group = new Group();
        group.setName(groupName);
        group.setAdmins(createTestUserList("admin", numberOfAdmins));
        group.setMembers(createTestUserList("user", numberOfMembers));

        return group;
    }

    private List<String> createTestUserList(String start, int numberOfUsers) {
        List<String> newUsers = new ArrayList<>();

        for (int i = 0; i < numberOfUsers; i++) {
            newUsers.add(String.format("%s%d@gmail.com", start, i));
        }
        return newUsers;
    }

    private Restaurant createRestaurant(String name) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(name);
        restaurant.setLink("google.com");
        return restaurant;
    }

    private Product createProduct(String name) {
        Product product = new Product();
        product.setName(name);
        product.setDescription("Description");
        product.setPrice(10D);
        product.setSaveDate(new DateTime().getMillis());
        product.setUserEmail("user0@gmail.com");
        return product;
    }

    private Order createOrder(Long groupId, Long restaurantId, Product product) {
        Order order = new Order();

        List<Product> productList = new ArrayList<>();
        productList.add(product);

        order.setProductList(productList);
        order.setGroupId(groupId);
        order.setRestaurantId(restaurantId);
        return order;
    }
}
