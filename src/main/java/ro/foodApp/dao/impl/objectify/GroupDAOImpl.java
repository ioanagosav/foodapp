package ro.foodApp.dao.impl.objectify;

import com.google.inject.Singleton;
import com.googlecode.objectify.Key;
import ro.foodApp.dao.GroupDAO;
import ro.foodApp.entities.Group;

import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Singleton
public class GroupDAOImpl implements GroupDAO {


    @Override
    public void saveGroup(Group group) {
        ofy().save().entity(group).now();
    }

    @Override
    public void addMembers(List<String> newUsers, String groupName) {
        Group group = getGroupByName(groupName);
        group.getMembers().addAll(newUsers);
        saveGroup(group);
    }

    @Override
    public void addAdmins(List<String> newAdmins, String groupName) {
        Group group = getGroupByName(groupName);
        group.getAdmins().addAll(newAdmins);
        saveGroup(group);
    }

    @Override
    public void deleteGroup(String groupName) {
        //ofy().delete().key(Key.create(Group.class, groupName)).now();
        //return ofy().load().type(Group.class).filter("groupName", groupName).list();
    }

    @Override
    public void startOrderForGroup(Long groupId) {

    }

    @Override
    public Group getGroupById(Long groupId) {
        return ofy().load().type(Group.class).filterKey(Key.create(Group.class, groupId)).first().now();
    }

    @Override
    public Group getGroupByName(String groupName) {
        List<Group> groups = ofy().load().type(Group.class).filter("name", groupName).list();
        if (groups.size() >= 1) {
            return groups.get(0);
        }
        return null;
    }

    @Override
    public List<Group> getGroupsForAdmin(String email) {
        return ofy().load().type(Group.class).filter("admins", email).list();
    }

    @Override
    public List<Group> getGroupsForUser(String email) {
        return ofy().load().type(Group.class).filter("members", email).list();
    }

    @Override
    public Boolean isUserAdminForGroup(Long groupId, String email) {
        Group group = this.getGroupById(groupId);
        if (null != group) {
            for (String adminEmail : group.getAdmins()) {
                if (email.equals(adminEmail)) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public Group deactivateOrderForGroup(Long groupId) {
        Group group = this.getGroupById(groupId);
        group.setHasActiveOrder(Boolean.FALSE);
        this.saveGroup(group);
        return group;
    }

}
