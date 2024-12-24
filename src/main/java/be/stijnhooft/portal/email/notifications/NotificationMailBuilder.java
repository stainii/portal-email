package be.stijnhooft.portal.email.notifications;

import be.stijnhooft.portal.model.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Collection;

@Service
public class NotificationMailBuilder {

    private TemplateEngine templateEngine;

    @Value("${mail.recipient}")
    private String recipient;

    @Value("${mail.sender}")
    private String sender;

    @Autowired
    public NotificationMailBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public MimeMessagePreparator build(Collection<Notification> notifications) {
        return mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(sender);
            messageHelper.setTo(recipient);
            messageHelper.setSubject("Notifications from the portal");
            messageHelper.setText(buildContent(notifications), true);
        };
    }

    private String buildContent(Collection<Notification> notifications) {
        Context context = new Context();
        context.setVariable("notifications", notifications);
        return templateEngine.process("notifications", context);
    }

}
