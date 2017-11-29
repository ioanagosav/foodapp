import org.joda.time.DateTime;
import ro.foodApp.entities.Group;
import ro.foodApp.entities.Product;
import ro.foodApp.entities.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    public static Group createTestGroup(String groupName, int numberOfAdmins, int numberOfMembers) {
        Group group = new Group();
        group.setName(groupName);
        group.setAdmins(createTestUserList("admin", numberOfAdmins));
        group.setMembers(createTestUserList("user", numberOfMembers));
        group.setHasActiveOrder(true);

        return group;
    }

    public static List<String> createTestUserList(String start, int numberOfUsers) {
        List<String> newUsers = new ArrayList<>();

        for (int i = 0; i < numberOfUsers; i++) {
            newUsers.add(String.format("%s%d@gmail.com", start, i));
        }
        return newUsers;
    }

    public static Restaurant createRestaurant(String name) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(name);
        restaurant.setLink("google.com");
        return restaurant;
    }

    public static Product createProduct(String name) {
        Product product = new Product();
        product.setName(name);
        product.setDescription("Description");
        product.setPrice(10D);
        product.setSaveDate(new DateTime().getMillis());
        product.setUserEmail("user0@gmail.com");
        return product;
    }
}
