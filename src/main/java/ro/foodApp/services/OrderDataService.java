package ro.foodApp.services;

import com.google.inject.Inject;
import ro.foodApp.dao.GroupDAO;
import ro.foodApp.dao.OrderDAO;
import ro.foodApp.entities.Group;
import ro.foodApp.entities.Order;
import ro.foodApp.mail.MailTaskCreator;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.logging.Logger;

@Path("/orders")
public class OrderDataService {

    private final OrderDAO orderDao;
    private final GroupDAO groupDao;
    private static final Logger LOG = Logger.getLogger(OrderDataService.class.getName());

    @Inject
    private OrderDataService(OrderDAO orderDao, GroupDAO groupDao) {
        this.orderDao = orderDao;
        this.groupDao = groupDao;
    }

    @POST
    @Path("/saveOrder")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Long saveOrder(Order order) {
        return this.orderDao.saveOrder(order);
    }

    @POST
    @Path("/getOrderById")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Order getOrderById(Long orderId) {
        return orderDao.getOrderById(orderId);
    }


    @POST
    @Path("/getOrderForGroup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Order getActiveOrderForGroup(Long groupId) {
        return this.orderDao.getActiveOrderForGroup(groupId);
    }

    @POST
    @Path("/sendOrder")
    @Consumes(MediaType.APPLICATION_JSON)
    public void sendOrder(Long id) {
        LOG.info("Entered method sendOrder");
        Order order = this.orderDao.sendOrder(id);
        Group group = groupDao.getGroupById(order.getGroupId());
        if (order.getProductList() != null && !order.getProductList().isEmpty()) {
            MailTaskCreator.createOrderConfirmationMailTask(group.getAdmins(), order.getProductList(), group.getName());
        } else {
            MailTaskCreator.createOrderFailureMailTask(group.getAdmins(), group.getName());
        }

    }

}
