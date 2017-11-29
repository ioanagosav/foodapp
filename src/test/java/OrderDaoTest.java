import static org.junit.Assert.assertEquals;

import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.inject.Inject;
import com.googlecode.objectify.ObjectifyService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import ro.foodApp.dao.GroupDAO;
import ro.foodApp.dao.OrderDAO;
import ro.foodApp.dao.RestaurantDAO;
import ro.foodApp.di.ApplicationModule;
import ro.foodApp.entities.*;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(GuiceTestRunner.class)
@GuiceModules({ApplicationModule.class})
public class OrderDaoTest {

    static {
        ObjectifyService.register(Order.class);
        ObjectifyService.register(Restaurant.class);
        ObjectifyService.register(Group.class);
    }

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    private Closeable closeable;
    private final Group group = TestUtils.createTestGroup("testGroup", 1, 2);
    private final Restaurant restaurant = TestUtils.createRestaurant("restaurantNameInput");
    private Product product = TestUtils.createProduct("productName");
    @Inject
    private GroupDAO groupDao;
    @Inject
    private RestaurantDAO restaurantDao;
    @Inject
    private OrderDAO orderDao;

    @Before
    public void setUp() {
        closeable = ObjectifyService.begin();
        helper.setUp();
    }

    @After
    public void tearDown() throws IOException {
        closeable.close();
        helper.tearDown();
    }


    @Test
    public void saveOrderTest() {

        try {
            groupDao.saveGroup(group);
            restaurantDao.saveRestaurant(restaurant);
            Order order = createOrder(group.getId(),restaurant.getId());
            orderDao.saveOrder(order);

            Order secondOrder = orderDao.getActiveOrderForGroup(group.getId());
            assertEquals(order, secondOrder);

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            System.out.println(exception.getStackTrace());
        }

    }


    private Order createOrder(Long groupId, Long restaurantId) {
        Order order = new Order();

        List<Product> productList = new ArrayList<>();
        productList.add(product);

        order.setProductList(productList);
        order.setGroupId(groupId);
        order.setRestaurantId(restaurantId);
        order.setActive(Boolean.TRUE);
        return order;
    }

}
