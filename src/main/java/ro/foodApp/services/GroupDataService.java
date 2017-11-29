package ro.foodApp.services;

import org.apache.commons.validator.routines.EmailValidator;
import com.google.appengine.api.users.UserService;
import com.google.inject.Inject;
import ro.foodApp.dao.GroupDAO;
import ro.foodApp.entities.Group;
import ro.foodApp.exception.InvalidEmailException;
import ro.foodApp.mail.MailTaskCreator;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/groups")
public class GroupDataService {

    private static final Logger LOG = Logger.getLogger(GroupDataService.class.getName());
    private final GroupDAO groupDao;

    private final UserService userService;

    @Inject
    private GroupDataService(GroupDAO groupDao, UserService userService) {
        this.groupDao = groupDao;
        this.userService = userService;
    }

    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveGroup(Group group) {
        groupDao.saveGroup(group);
    }

    @POST
    @Path("/saveNewGroup")
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveNewGroup(Group group) {
        validateEmails(group.getAdmins());
        validateEmails(group.getMembers());
        groupDao.saveGroup(group);
    }

    @POST
    @Path("/startOrderForGroup")
    public void startOrderForGroup(Long groupId) {
        groupDao.startOrderForGroup(groupId);
    }

    @GET
    @Path("/getGroupsForAdmin")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Group> getGroupsForAdmin() {
        if (userService.getCurrentUser() == null) {
            return null;
        }
        List<Group> groups = new ArrayList<>();
        try {
            groups = groupDao.getGroupsForAdmin(userService.getCurrentUser().getEmail());
        } catch (IllegalArgumentException exception) {
            LOG.log(Level.INFO, "No groups for admin " + userService.getCurrentUser().getEmail());
        }
        return groups;
    }

    @GET
    @Path("/getGroupsForUser")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Group> getGroupsForUser() {
        if (userService.getCurrentUser() == null) {
            return null;
        }
        List<Group> groups = new ArrayList<>();
        try {
            groups = groupDao.getGroupsForUser(userService.getCurrentUser().getEmail());
        } catch (IllegalArgumentException exception) {
            LOG.log(Level.INFO, "No groups for user " + userService.getCurrentUser().getEmail());
        }
        return groups;
    }

    @POST
    @Path("/getGroupById")
    @Produces(MediaType.APPLICATION_JSON)
    public Group getGroupById(Long groupId) {
        return groupDao.getGroupById(groupId);
    }

    @POST
    @Path("/isUserAdminForGroup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean isUserAdminOfGroup(Long groupId) {
        String email = userService.getCurrentUser().getEmail();
        return groupDao.isUserAdminForGroup(groupId, email);
    }

    @POST
    @Path("/deactivateGroup")
    @Consumes(MediaType.APPLICATION_JSON)
    public void deactivateGroup(Long groupId) {
        groupDao.deactivateOrderForGroup(groupId);
    }

    @POST
    @Path("/inviteMembers")
    @Consumes(MediaType.APPLICATION_JSON)
    public void inviteMembers(List<String> members) {
        LOG.info("Received members" + members.toString());
        MailTaskCreator.createMemberInvitationMailTask(members);
    }

    @POST
    @Path("/inviteAdmins")
    @Consumes(MediaType.APPLICATION_JSON)
    public void inviteAdmins(List<String> admins) {
        LOG.info("Received admins" + admins.toString());
        MailTaskCreator.createAdminInvitationMailTask(admins);
    }

    private void validateEmails(List<String> emails) {
        EmailValidator validator = EmailValidator.getInstance();
        for (String email : emails) {
            if (!validator.isValid(email)) {
                throw new InvalidEmailException(email + " is not a valid email.");
            }
        }
    }

}
