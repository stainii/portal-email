package be.stijnhooft.portal.notifications.plugins.email.services;

import be.stijnhooft.portal.notifications.model.Notification;
import be.stijnhooft.portal.notifications.plugins.email.routes.NotificationRoute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Collection;

@Service
@Transactional
@Slf4j
public class NotificationService {

    private final MailBuilder mailBuilder;
    private final JavaMailSender mailSender;

    @Inject
    public NotificationService(MailBuilder mailBuilder, JavaMailSender mailSender) {
        this.mailBuilder = mailBuilder;
        this.mailSender = mailSender;
    }

    /**
     * Invoked by {@link NotificationRoute#configure()}
     */
    public void receiveNotificationsAndSendMail(Collection<Notification> notifications) {
        MimeMessagePreparator mail = mailBuilder.build(notifications);
        mailSender.send(mail);

        log.info("Sent mail for " + notifications.size() + " notifications.");
        log.debug(notifications.toString());
    }

}
