package be.stijnhooft.portal.email.notifications;

import be.stijnhooft.portal.model.notification.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableBinding(NotificationTopic.class)
@Slf4j
public class NotificationListener {

    private final NotificationService notificationService;

    public NotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @StreamListener(NotificationTopic.INPUT)
    public void log(List<Notification> notifications) {
        log.info("Received {} notifications for which a mail needs to be sent", notifications.size());
        log.debug(notifications.toString());
        notificationService.receiveNotificationsAndSendMail(notifications);
    }

}
