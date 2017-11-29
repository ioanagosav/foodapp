package ro.foodApp.di;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import ro.foodApp.dao.GroupDAO;
import ro.foodApp.dao.OrderDAO;
import ro.foodApp.dao.RestaurantDAO;

import java.util.Set;

public class ApplicationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(GroupDAO.class)
                .to(ro.foodApp.dao.impl.objectify.GroupDAOImpl.class);
        /*bind(GroupDAO.class)
                .annotatedWith(Names.named("entity"))
                .to(ro.foodApp.dao.impl.entity.GroupDAOImpl.class);*/
        bind(RestaurantDAO.class).to(ro.foodApp.dao.impl.objectify.RestaurantDAOImpl.class);
        bind(OrderDAO.class).to(ro.foodApp.dao.impl.objectify.OrderDAOImpl.class);
    }

    @Provides
    private UserService createUserService() {
        return UserServiceFactory.getUserService();
    }

    /*@Provides
    @Named("mocked")
    private UserService createMockedUserService() {
        return new DevMockUser();
    }

    private static class DevMockUser implements UserService {

        @Override
        public String createLoginURL(String s) {
            return null;
        }

        @Override
        public String createLoginURL(String s, String s1) {
            return null;
        }

        @Override
        public String createLoginURL(String s, String s1, String s2, Set<String> set) {
            return null;
        }

        @Override
        public String createLogoutURL(String s) {
            return null;
        }

        @Override
        public String createLogoutURL(String s, String s1) {
            return null;
        }

        @Override
        public boolean isUserLoggedIn() {
            return true;
        }

        @Override
        public boolean isUserAdmin() {
            return true;
        }

        @Override
        public User getCurrentUser() {
            return new User("test@gmail.com", "gmail.com");
        }
    }*/
}
