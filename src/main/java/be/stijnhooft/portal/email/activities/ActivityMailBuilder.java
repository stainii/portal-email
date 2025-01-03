package be.stijnhooft.portal.email.activities;

import be.stijnhooft.portal.model.domain.Event;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Collection;

@Service
public class ActivityMailBuilder {

    private TemplateEngine templateEngine;

    @Value("${mail.recipient}")
    private String recipient;

    @Value("${mail.sender}")
    private String sender;

    public ActivityMailBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public MimeMessagePreparator build(Collection<Event> events) {
        return mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(sender);
            messageHelper.setTo(recipient);
            messageHelper.setSubject("Suggestions for this weekend");
            messageHelper.setText(buildContent(events), true);
        };
    }

    private String buildContent(Collection<Event> events) {
        Context context = new Context();
        context.setVariable("events", events);
        return templateEngine.process("activity", context);
    }

}
