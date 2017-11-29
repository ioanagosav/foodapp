package ro.foodApp.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
public class Restaurant {

    @Id
    private Long id;
    @Index
    private String name;
    private String link;

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Restaurant))
            return false;
        if (obj == this)
            return true;

        Restaurant restaurant = (Restaurant) obj;
        return new EqualsBuilder().
                append(this.name, restaurant.name).
                append(this.link, restaurant.link).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(name).
                append(link).
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
