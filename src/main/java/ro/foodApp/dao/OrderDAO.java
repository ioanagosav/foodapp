package ro.foodApp.dao;

import ro.foodApp.entities.*;

import java.util.List;
import java.util.Set;

public interface OrderDAO {

    Long saveOrder(Order order);

    Order getActiveOrderForGroup(Long groupId);

    Order sendOrder(Long orderId);

    Order getOrderById(Long orderId);

    List<Order> getAllOrdersForGroup(Long groupId);
}
