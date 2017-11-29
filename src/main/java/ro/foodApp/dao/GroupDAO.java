package ro.foodApp.dao;

import ro.foodApp.entities.Group;

import java.util.List;

public interface GroupDAO {

    void saveGroup(Group group);

    void addMembers(List<String> newUsers, String groupName);

    void addAdmins(List<String> newAdmins, String groupName);

    void deleteGroup(String groupName);

    void startOrderForGroup(Long groupId);

    Group getGroupByName(String groupName);

    Group getGroupById(Long groupId);

    List<Group> getGroupsForAdmin(String email);

    List<Group> getGroupsForUser(String email);

    Boolean isUserAdminForGroup(Long groupId, String email);

    Group deactivateOrderForGroup(Long groupId);
}
