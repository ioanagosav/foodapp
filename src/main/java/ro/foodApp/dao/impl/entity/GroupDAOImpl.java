package ro.foodApp.dao.impl.entity;

import com.google.appengine.api.datastore.*;
import ro.foodApp.dao.GroupDAO;
import ro.foodApp.entities.*;

import java.util.ArrayList;
import java.util.List;

public class GroupDAOImpl implements GroupDAO {

    DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();


    @Override
    public void saveGroup(Group group) {
        Key groupKey = KeyFactory.createKey("Group", group.getName());
        Entity groupEntity = new Entity(groupKey);
        groupEntity.setProperty("name", group.getName());
        groupEntity.setProperty("admins", group.getAdmins());
        groupEntity.setProperty("members", group.getMembers());
        groupEntity.setProperty("hasActiveOrder", group.getHasActiveOrder());
        Transaction transaction = datastoreService.beginTransaction();
        datastoreService.put(transaction, groupEntity);
        transaction.commit();
    }

    @Override
    public void addMembers(List<String> newUsers, String groupName) {
        Entity groupEntity = getGroupEntityByNameKey(groupName);
        List<String> oldUserList = (List<String>) groupEntity.getProperty("members");
        if (oldUserList == null) {
            oldUserList = new ArrayList<>();
        }
        oldUserList.addAll(newUsers);
        groupEntity.setProperty("members", oldUserList);
        Transaction transaction = datastoreService.beginTransaction();
        datastoreService.put(transaction, groupEntity);
        transaction.commit();
    }

    @Override
    public void addAdmins(List<String> newAdmins, String groupName) {
        Entity groupEntity = getGroupEntityByNameKey(groupName);
        List<String> oldAdminList = (List<String>) groupEntity.getProperty("admins");
        if (oldAdminList == null) {
            oldAdminList = new ArrayList<>();
        }
        oldAdminList.addAll(newAdmins);
        groupEntity.setProperty("admins", oldAdminList);
        Transaction transaction = datastoreService.beginTransaction();
        datastoreService.put(transaction, groupEntity);
        transaction.commit();
    }

    private Entity getGroupEntityByNameKey(String groupName) {
        Key groupKey = KeyFactory.createKey("Group", groupName);
        Entity groupEntity = null;
        try {
            Transaction transaction = datastoreService.beginTransaction();
            groupEntity = datastoreService.get(transaction, groupKey);
            transaction.commit();
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return groupEntity;
    }

    @Override
    public void deleteGroup(String groupName) {
        Transaction transaction = datastoreService.beginTransaction();
        datastoreService.delete(transaction, KeyFactory.createKey("Group", groupName));
        transaction.commit();
    }

    @Override
    public void startOrderForGroup(Long groupId) {

    }

    @Override
    public Group getGroupByName(String groupName) {
        Group foundGroup = new Group();
        Entity groupEntity = getGroupEntityByNameKey(groupName);

        if (groupEntity != null) {
            foundGroup.setName((String) groupEntity.getProperty("name"));
            foundGroup.setAdmins((List<String>) groupEntity.getProperty("admins"));
            foundGroup.setMembers((List<String>) groupEntity.getProperty("members"));
        }
        return foundGroup;
    }

    @Override
    public Group getGroupById(Long groupId) {
        return null;
    }

    @Override
    public List<Group> getGroupsForAdmin(String email) {
        return null;
    }

    @Override
    public List<Group> getGroupsForUser(String email) {
        return null;
    }

    @Override
    public Boolean isUserAdminForGroup(Long groupId, String email) {
        return null;
    }

    @Override
    public Group deactivateOrderForGroup(Long groupId) {
        return null;
    }
}
