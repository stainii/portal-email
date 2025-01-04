package be.stijnhooft.portal.email.events;

import be.stijnhooft.portal.email.activities.ActivityService;
import be.stijnhooft.portal.model.domain.Event;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Configuration
@Slf4j
@AllArgsConstructor
public class EventListener {

    private final ActivityService activityService;

    @Bean
    public Consumer<List<Event>> eventChannel() {
        return events -> {
            log.info("Received {} events", events.size());
            log.debug("{}", events);

            handleActivitySuggestions(events);
        };
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
