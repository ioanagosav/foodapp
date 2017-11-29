package ro.foodApp.dao.impl.objectify;

import com.google.appengine.api.datastore.Query.*;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import org.joda.time.DateTime;
import ro.foodApp.dao.OrderDAO;
import ro.foodApp.entities.Order;
import ro.foodApp.entities.Product;
import ro.foodApp.entities.Restaurant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrderDAOImpl implements OrderDAO {

    @Override
    public Long saveOrder(Order order) {
        Object orderObject = ObjectifyService.ofy().save().entity(order).now();
        return ((Key) orderObject).getId();
    }

    @Override
    public Order sendOrder(Long orderId) {
        Order order = getOrderById(orderId);
        Long now = (new DateTime()).getMillis();
        order.setActive(Boolean.FALSE);
        order.setSendOrderDate(now);
        saveOrder(order);
        return order;
    }

    @Override
    public Order getActiveOrderForGroup(Long groupId) {
        Filter activeOrderFilter = new FilterPredicate("active", FilterOperator.EQUAL, Boolean.TRUE);
        Filter orderForGroupFilter = new FilterPredicate("groupId", FilterOperator.EQUAL, groupId);

        CompositeFilter activeOrderForGroupFilter = CompositeFilterOperator.and(activeOrderFilter, orderForGroupFilter);
        List<Order> orderList = ObjectifyService.ofy().load().type(Order.class).filter(activeOrderForGroupFilter).list();

        if (orderList != null && orderList.size() >= 1) {
            return orderList.get(0);
        }
        return null;
    }

    @Override
    public Order getOrderById(Long orderId) {
        return ObjectifyService.ofy().load().key(Key.create(Order.class, orderId)).now();
    }

    @Override
    public List<Order> getAllOrdersForGroup(Long groupId) {
        List<Order> orders;
        orders = ObjectifyService.ofy().load().type(Order.class).filter("groupId", groupId).list();
        return orders;
    }

}
