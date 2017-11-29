package ro.foodApp.mail;

import ro.foodApp.entities.Product;

import java.util.List;

public class MailTemplate {

    private final static String APP_ADDRESS = "https://manage-food-order.appspot.com";
    private final static String MAIL_BODY_SIGNATURE = "\n\n-----------------\nThank you, please come again.\n";

    static String orderConfirmationSubject(String groupName) {
        return "Order saved today for your group: " + groupName;
    }

    static String orderConfirmationBody(List<Product> productList) {
        StringBuilder sb = new StringBuilder();
        sb.append("Hello! This is what your team ordered today: \n\n ");
        Double total = 0D;

        for (Product p : productList) {
            total += p.getPrice();
            sb.append(p.getUserEmail());
            sb.append(" ordered ");
            sb.append(p.getName());
            sb.append(" = ");
            sb.append(p.getPrice());
            sb.append(" lei. ");
            if (!p.getDescription().isEmpty() || null != p.getDescription()) {
                sb.append("DETAILS: ");
                sb.append(p.getDescription());
            }
            sb.append("\n");
        }

        sb.append("\n-----------------\n");
        sb.append("TOTAL: ");
        sb.append(total);
        sb.append(MAIL_BODY_SIGNATURE);

        return sb.toString();
    }

    static String orderFailureSubject(String groupName) {
        return "Problem regarding your latest order for your group: " + groupName;
    }

    static String orderFailureBody() {
        return "You did not add any products to the order.\n"
                + MAIL_BODY_SIGNATURE;
    }

    static String inviteMemberSubject() {
        return "Invitation to order food";
    }

    static String inviteMemberBody() {
        return "You have been invited to join a group to order food with your colleagues. \n"
                + "Go to " + APP_ADDRESS + " to start ordering." + MAIL_BODY_SIGNATURE;
    }

    static String inviteAdminSubject() {
        return "Invitation to manage food order";
    }

    static String inviteAdminBody() {
        return "You have been invited to manage a group's food order for your colleagues. \n"
                + "Go to " + APP_ADDRESS + " to start your order." + MAIL_BODY_SIGNATURE;
    }
}
