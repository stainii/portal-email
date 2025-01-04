package be.stijnhooft.portal.email.notifications;

import be.stijnhooft.portal.model.notification.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Consumer;

@Configuration
@Slf4j
public class NotificationListener {

    private final NotificationService notificationService;

    public NotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Bean
    public Consumer<List<Notification>> notificationChannel() {
        return notifications -> {
            log.info("Received {} notifications for which a mail needs to be sent", notifications.size());
            log.debug(notifications.toString());
            notificationService.receiveNotificationsAndSendMail(notifications);
        };
    }

}
