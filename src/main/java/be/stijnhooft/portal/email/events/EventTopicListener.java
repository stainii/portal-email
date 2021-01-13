package be.stijnhooft.portal.email.events;

import be.stijnhooft.portal.email.activities.ActivityService;
import be.stijnhooft.portal.model.domain.Event;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@EnableBinding(EventTopic.class)
@Slf4j
@AllArgsConstructor
public class EventTopicListener {

    private final ActivityService activityService;

    @StreamListener(EventTopic.INPUT)
    public void receive(List<Event> events) {
        log.info("Received {} events", events.size());
        log.debug("{}", events);

        handleActivitySuggestions(events);
    }

    private void handleActivitySuggestions(List<Event> events) {
        var activitySuggestions = events.stream()
            .filter(event -> event.getSource().equals("Activity"))
            .collect(Collectors.toList());
        log.info("... of which {} are activity suggestions", activitySuggestions.size());

        if (!activitySuggestions.isEmpty()) {
            activityService.receiveSuggestionsAndSendMail(activitySuggestions);
        }
    }

}
