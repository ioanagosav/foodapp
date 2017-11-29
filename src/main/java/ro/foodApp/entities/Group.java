package ro.foodApp.entities;

import java.util.List;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
public class Group {
    @Id
    private Long id;
    @Index
    private String name;
    @Index
    private List<String> admins;
    @Index
    private List<String> members;
    private Boolean notifyMembers;
    private Boolean hasActiveOrder;


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Group))
            return false;
        if (obj == this)
            return true;

        Group group = (Group) obj;
        return new EqualsBuilder().
                append(this.name, group.name).
                append(this.id, group.id).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(name).
                append(id).
                toHashCode();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getNotifyMembers() {
        return notifyMembers;
    }

    public void setNotifyMembers(Boolean notifyMembers) {
        this.notifyMembers = notifyMembers;
    }

    public List<String> getAdmins() {
        return admins;
    }

    public void setAdmins(List<String> admins) {
        this.admins = admins;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public Boolean getHasActiveOrder() {
        return hasActiveOrder;
    }

    public void setHasActiveOrder(Boolean hasActiveOrder) {
        this.hasActiveOrder = hasActiveOrder;
    }
}