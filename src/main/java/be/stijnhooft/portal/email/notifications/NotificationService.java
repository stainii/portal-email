package be.stijnhooft.portal.email.notifications;

import be.stijnhooft.portal.model.notification.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional
@Slf4j
public class NotificationService {

    private final NotificationMailBuilder notificationMailBuilder;
    private final JavaMailSender mailSender;

    public NotificationService(NotificationMailBuilder notificationMailBuilder, JavaMailSender mailSender) {
        this.notificationMailBuilder = notificationMailBuilder;
        this.mailSender = mailSender;
    }

    public void receiveNotificationsAndSendMail(Collection<Notification> notifications) {
        MimeMessagePreparator mail = notificationMailBuilder.build(notifications);
        mailSender.send(mail);

        log.info("Sent mail for {} notifications.", notifications.size());
        log.debug("{}", notifications);
    }

}
