package be.stijnhooft.portal.notifications.plugins.email.messaging;

import be.stijnhooft.portal.notifications.model.Notification;
import be.stijnhooft.portal.notifications.plugins.email.services.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableBinding(NotificationTopic.class)
@Slf4j
public class NotificationListener {

    private final NotificationService notificationService;

    @Autowired
    public NotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @StreamListener(NotificationTopic.INPUT)
    public void log(List<Notification> notifications) {
        log.info("Received notifications for which a mail needs to be sent");
        log.debug(notifications.toString());
        notificationService.receiveNotificationsAndSendMail(notifications);
    }

}
