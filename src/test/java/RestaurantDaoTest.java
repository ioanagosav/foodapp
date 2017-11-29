
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
import ro.foodApp.dao.RestaurantDAO;
import ro.foodApp.di.ApplicationModule;
import ro.foodApp.entities.Restaurant;

import java.io.Closeable;
import java.io.IOException;

@RunWith(GuiceTestRunner.class)
@GuiceModules({ApplicationModule.class})
public class RestaurantDaoTest {
    static {
        ObjectifyService.register(Restaurant.class);
    }

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    private Closeable closeable;

    @Inject
    private RestaurantDAO restaurantDao;

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
    public void saveRestaurant() {
        Restaurant restaurant = TestUtils.createRestaurant("testRestaurant");
        restaurantDao.saveRestaurant(restaurant);
        Restaurant secondRestaurant = restaurantDao.getRestaurantByName("testRestaurant");
        assertEquals(restaurant, secondRestaurant);
    }


}
