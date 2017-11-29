package ro.foodApp.di;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.googlecode.objectify.ObjectifyService;
import com.squarespace.jersey2.guice.JerseyGuiceModule;
import com.squarespace.jersey2.guice.JerseyGuiceUtils;
import ro.foodApp.entities.*;

public class GuiceConfig extends GuiceServletContextListener {

    static {
        ObjectifyService.register(Group.class);
        ObjectifyService.register(Restaurant.class);
        ObjectifyService.register(Order.class);
    }

    @Override
    protected Injector getInjector() {
        Injector injector = Guice.createInjector(
                new JerseyGuiceModule("__HK2_Generated_0"),
                new CustomServletModule(),
                new ApplicationModule()
        );
        JerseyGuiceUtils.install(injector);

        return injector;
    }
}
