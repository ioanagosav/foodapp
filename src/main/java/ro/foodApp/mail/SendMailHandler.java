package ro.foodApp.mail;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class SendMailHandler extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(SendMailHandler.class.getName());
    private static final String EMAIL_FROM = "manage.food.order@gmail.com";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String emailTo = request.getParameter("email");
        String emailSubject = request.getParameter("subject");
        String emailBody = request.getParameter("body");

        LOG.info("Trying to send email");
        SendMail.sendMail(EMAIL_FROM, emailTo, emailTo, emailSubject, emailBody);
    }
}
