package ro.foodApp.mail;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import ro.foodApp.entities.Product;

import java.util.List;
import java.util.logging.Logger;

public class MailTaskCreator {

    private static final Logger LOG = Logger.getLogger(MailTaskCreator.class.getName());

    public static void createOrderConfirmationMailTask(List<String> recipientList, List<Product> productList, String groupName) {
        LOG.info("Sending ORDER_CONFIRMATION_TEMPLATE mail");
        for (String adminEmail : recipientList) {
            sendOrderConfirmationEmail(
                    adminEmail,
                    MailTemplate.orderConfirmationSubject(groupName),
                    MailTemplate.orderConfirmationBody(productList)
            );
        }
    }

    public static void createOrderFailureMailTask(List<String> recipientList, String groupName) {
        LOG.info("Sending ORDER_FAILURE_TEMPLATE mail");
        for (String adminEmail : recipientList) {
            sendOrderConfirmationEmail(
                    adminEmail,
                    MailTemplate.orderFailureSubject(groupName),
                    MailTemplate.orderFailureBody()
            );
        }
    }

    public static void createMemberInvitationMailTask(List<String> recipientList) {
        LOG.info("Sending MEMBER_INVITATION_TEMPLATE mail");
        for (String adminEmail : recipientList) {
            sendOrderConfirmationEmail(
                    adminEmail,
                    MailTemplate.inviteMemberSubject(),
                    MailTemplate.inviteMemberBody()
            );
        }
    }

    public static void createAdminInvitationMailTask(List<String> recipientList) {
        LOG.info("Sending ADMIN_INVITATION_TEMPLATE mail");
        for (String adminEmail : recipientList) {
            sendOrderConfirmationEmail(
                    adminEmail,
                    MailTemplate.inviteAdminSubject(),
                    MailTemplate.inviteAdminBody()
            );
        }
    }

    private static void sendOrderConfirmationEmail(String email, String emailSubject, String emailBody) {
        Queue queue = QueueFactory.getDefaultQueue();
        queue.add(
                TaskOptions.Builder
                        .withMethod(TaskOptions.Method.GET).url("/sendMailHandler")
                        .param("email", email)
                        .param("body", emailBody)
                        .param("subject", emailSubject)
        );
        LOG.info("Added send mail (to " + email + ") action to task queue default");
    }
}
