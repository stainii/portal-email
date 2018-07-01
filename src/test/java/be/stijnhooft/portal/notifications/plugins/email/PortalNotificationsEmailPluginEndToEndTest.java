package be.stijnhooft.portal.notifications.plugins.email;

import be.stijnhooft.portal.notifications.model.Notification;
import be.stijnhooft.portal.notifications.model.NotificationAction;
import be.stijnhooft.portal.notifications.model.NotificationUrgency;
import be.stijnhooft.portal.notifications.plugins.email.services.NotificationService;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class PortalNotificationsEmailPluginEndToEndTest {

    @Inject
    private NotificationService notificationService;

    private GreenMail smtpServer;

    @Before
    public void setUp() {
        smtpServer = new GreenMail(new ServerSetup(2225, null, "smtp"));
        smtpServer.start();
    }

    @After
    public void tearDown() throws Exception {
        smtpServer.stop();
    }

    @Test
    public void receiveNotificationsAndSendMail() throws IOException, MessagingException {
        // data set
        Notification notification1 = new Notification(null, "Housagotchi",
            LocalDateTime.of(2018, 4, 29, 10, 0), "notification 1", "hurry up!",
            NotificationUrgency.PUBLISH_IMMEDIATELY,
            new NotificationAction("http://www.stijnhooft.be", "Open it up"));
        Notification notification2 = new Notification(null, "Article writer",
            LocalDateTime.of(2018, 4, 29, 11, 13), "notification 2", "chill...",
            NotificationUrgency.PUBLISH_IMMEDIATELY,
            new NotificationAction("http://portal.stijnhooft.be", "Do something about it"));
        List<Notification> notifications = Arrays.asList(notification1, notification2);

        // execute
        notificationService.receiveNotificationsAndSendMail(notifications);

        //assert
        assertReceivedMessageContains("<!DOCTYPE html>\n" +
            "\n" +
            "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head></head>\n" +
            "<body>\n" +
            "<h1>Template</h1>\n" +
            "<span>hurry up!</span>\n" +
            "</body>\n" +
            "</html>\n");
    }

    private void assertReceivedMessageContains(String expected) throws IOException, MessagingException {
        MimeMessage[] receivedMessages = smtpServer.getReceivedMessages();
        assertEquals(1, receivedMessages.length);
        String actual = (String) receivedMessages[0].getContent();
        assertEquals(expected.replaceAll("\\r\\n?", "\n"), actual.replaceAll("\\r\\n?", "\n"));
    }
}
