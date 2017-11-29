package ro.foodApp.di;


import com.google.inject.servlet.ServletModule;
import ro.foodApp.authorization.AppEngineAuthorization;
import ro.foodApp.authorization.OAuth2Callback;
import ro.foodApp.di.responseFilter.CORSResponseFilter;

public class CustomServletModule extends ServletModule {

    @Override
    public void configureServlets() {
        filter("/*").through(CORSResponseFilter.class);
        serve("oauth2callback/*").with(OAuth2Callback.class);
        serve("/").with(AppEngineAuthorization.class);
    }


}
