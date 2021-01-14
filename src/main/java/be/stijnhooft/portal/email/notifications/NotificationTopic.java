package be.stijnhooft.portal.email.notifications;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

public interface NotificationTopic {

    String INPUT = "notificationTopic";

    @Input(INPUT)
    MessageChannel notificationTopic();
}
