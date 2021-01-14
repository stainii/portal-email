package be.stijnhooft.portal.email.activities;

import be.stijnhooft.portal.model.domain.Event;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class ActivityService {

    private final ActivityMailBuilder activityMailBuilder;
    private final JavaMailSender mailSender;

    public void receiveSuggestionsAndSendMail(Collection<Event> events) {
        MimeMessagePreparator mail = activityMailBuilder.build(events);
        mailSender.send(mail);

        log.info("Sent mail for {} activity suggestions.", events.size());
        log.debug("{}", events);
    }

}
