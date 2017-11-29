package ro.foodApp.mail;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendMail {

    private static final Logger LOG = Logger.getLogger(SendMail.class.getName());

    static void sendMail(String sendEmailFrom, String sendMailTo, String recipientName, String messageSubject, String messageText) {
        Properties prop = new Properties();
        Session session = Session.getDefaultInstance(prop, null);
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(sendEmailFrom));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(sendMailTo, "Mr./Ms. " + recipientName));
            msg.setSubject(messageSubject);
            msg.setText(messageText);
            Transport.send(msg);
            LOG.info("Successful Delivery.");
        } catch (MessagingException | UnsupportedEncodingException e) {
            LOG.log(Level.WARNING, e.getMessage());
        }
    }
}