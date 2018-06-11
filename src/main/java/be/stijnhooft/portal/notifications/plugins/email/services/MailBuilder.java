package be.stijnhooft.portal.notifications.plugins.email.services;

import be.stijnhooft.portal.notifications.model.Notification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.inject.Inject;
import java.util.Collection;

@Service
public class MailBuilder {

    private TemplateEngine templateEngine;

    @Value("${mail.recipient}")
    private String recipient;

    @Value("${mail.sender}")
    private String sender;

    @Value("${mail.subject}")
    private String subject;

    @Inject
    public MailBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public MimeMessagePreparator build(Collection<Notification> notifications) {
        return mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(sender);
            messageHelper.setTo(recipient);
            messageHelper.setSubject(subject);
            messageHelper.setText(buildContent(notifications), true);
        };
    }

    private String buildContent(Collection<Notification> notifications) {
        Context context = new Context();
        context.setVariable("notifications", notifications);
        return templateEngine.process("mail-template", context);
    }

}
