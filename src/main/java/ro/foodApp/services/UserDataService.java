package ro.foodApp.services;

import com.google.appengine.api.users.UserService;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import ro.foodApp.dao.GroupDAO;
import ro.foodApp.dto.UserDto;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/me")
public class UserDataService {

    private final GroupDAO groupDao;

    private final UserService userService;

    @Inject
    private UserDataService(GroupDAO groupDao, UserService userService) {
        this.groupDao = groupDao;
        this.userService = userService;
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public UserDto getLoggedInUser() {
        if (userService.getCurrentUser() == null) {
            return null;
        }
        UserDto userDto = new UserDto();
        userDto.setEmail(userService.getCurrentUser().getEmail());
        userDto.setLogoutUrl(userService.createLogoutURL("/"));
        userDto.setNickname(userService.getCurrentUser().getNickname());
        userDto.setAdmin(false);

        return userDto;
    }

}
