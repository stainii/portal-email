package be.stijnhooft.portal.email.notifications;

import be.stijnhooft.portal.model.notification.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Consumer;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationService notificationService;

    @Bean
    public Consumer<List<Notification>> notificationChannel() {
        return notifications -> {
            log.info("Received {} notifications for which a mail needs to be sent", notifications.size());
            notificationService.receiveNotificationsAndSendMail(notifications);
        };
    }

/*    @Bean
    public Consumer<byte[]> notificationChannel() {
        return bytes -> {
            try {
                List<Notification> notifications = objectMapper.readValue(bytes, new TypeReference<List<Notification>>() {});
                log.info("Received {} notifications for which a mail needs to be sent", notifications.size());
                notificationService.receiveNotificationsAndSendMail(notifications);
            } catch (IOException e) {
                log.error("Failed to deserialize notifications", e);
            }
        };
    }*/

}
