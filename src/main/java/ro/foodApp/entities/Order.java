package ro.foodApp.entities;


import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
public class Order {
    @Id
    private Long id;
    private Long restaurantId;
    @Index
    private Long groupId;
    private List<Product> productList;
    private Long sendOrderDate;
    @Index
    private Boolean active;

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Order))
            return false;
        if (obj == this)
            return true;

        Order order = (Order) obj;
        return new EqualsBuilder().
                append(this.id, order.id).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(id).
                append(active).
                append(sendOrderDate).
                toHashCode();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getSendOrderDate() {
        return sendOrderDate;
    }

    public void setSendOrderDate(Long sendOrderDate) {
        this.sendOrderDate = sendOrderDate;
    }
}
