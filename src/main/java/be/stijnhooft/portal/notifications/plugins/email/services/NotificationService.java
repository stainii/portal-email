package be.stijnhooft.portal.notifications.plugins.email.services;

import be.stijnhooft.portal.notifications.model.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional
@Slf4j
public class NotificationService {

    private final MailBuilder mailBuilder;
    private final JavaMailSender mailSender;

    @Autowired
    public NotificationService(MailBuilder mailBuilder, JavaMailSender mailSender) {
        this.mailBuilder = mailBuilder;
        this.mailSender = mailSender;
    }

    public void receiveNotificationsAndSendMail(Collection<Notification> notifications) {
        MimeMessagePreparator mail = mailBuilder.build(notifications);
        mailSender.send(mail);

        log.info("Sent mail for " + notifications.size() + " notifications.");
        log.debug(notifications.toString());
    }

}
