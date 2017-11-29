package ro.foodApp.services;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.inject.Inject;
import ro.foodApp.dao.GroupDAO;
import ro.foodApp.entities.Group;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/test/group")
public class GroupTestController {

    private final GroupDAO groupDao;


    @Inject
    public GroupTestController(GroupDAO groupDao) {
        this.groupDao = groupDao;
    }


    @POST
    @Path("/addGroup")
    //@Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Group addGroup() throws Exception {
        Group group = getTestGroup();
        groupDao.saveGroup(group);
        group = groupDao.getGroupByName("testGroup");
        return group;
    }

    @POST
    @Path("/addGroupMembers")
    @Produces(MediaType.APPLICATION_JSON)
    public Group addGroupMembers() {
        Group group = groupDao.getGroupByName("testGroup");
        groupDao.addMembers(getTestUserList(), group.getName());
        return group;
    }

    @POST
    @Path("/addGroupAdmins")
    @Produces(MediaType.APPLICATION_JSON)
    public Group addGroupAdmins() {
        Group group = groupDao.getGroupByName("testGroup");
        groupDao.addAdmins(getTestUserList(), group.getName());
        return group;
    }

    @POST
    @Path("/deleteGroup")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteGroup() {
        Group group = groupDao.getGroupByName("testGroup");
        groupDao.deleteGroup(group.getName());
        return "{ \"Message\": \"Success\" }";
    }

    @POST
    @Path("/sendEmails")
    public void sendEmails() {
        Group group = groupDao.getGroupByName("testGroup");
        for (String userEmail : group.getMembers()) {
            createTaskQueueSendEmails(userEmail);
        }
    }

    @POST
    @Path("/testOrders")
    public void testOrder() {
        Group group = groupDao.getGroupByName("testGroup");
        for (String userEmail : group.getMembers()) {
            createTaskQueueSendEmails(userEmail);
        }
    }


    private List<String> getTestUserList() {
        List<String> newUsers = new ArrayList<>();
        newUsers.add("test3@gmail.com");
        newUsers.add("test4@gmail.com");
        newUsers.add("test5@gmail.com");
        newUsers.add("test6@gmail.com");
        return newUsers;
    }

    private Group getTestGroup() {
        Group group = new Group();

        List<String> newAdmins = new ArrayList<>();
        List<String> newMembers = new ArrayList<>();
        newAdmins.add("test10@gmail.com");
        newMembers.add("test12@gmail.com");
        group.setName("testGroup");

        group.setAdmins(newAdmins);
        group.setMembers(newMembers);

        return group;
    }

    private void createTaskQueueSendEmails(String email) {
        Queue queue = QueueFactory.getDefaultQueue();
        queue.add(TaskOptions.Builder.withUrl("/sendMailHandler").param("memberEmail", email));
    }

}
