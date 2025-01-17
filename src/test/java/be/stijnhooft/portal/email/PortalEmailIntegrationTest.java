package be.stijnhooft.portal.email;

import be.stijnhooft.portal.model.domain.Event;
import be.stijnhooft.portal.model.domain.FlowAction;
import be.stijnhooft.portal.model.notification.Notification;
import be.stijnhooft.portal.model.notification.NotificationAction;
import be.stijnhooft.portal.model.notification.PublishStrategy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class PortalEmailIntegrationTest {

    @Container
    @ServiceConnection
    static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3-management")
        .withReuse(true);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.stream.binders.rabbit.environment.spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
        registry.add("spring.cloud.stream.binders.rabbit.environment.spring.rabbitmq.host", rabbitMQContainer::getHost);
    }

    @RegisterExtension
    static GreenMailExtension smtpServer = new GreenMailExtension(ServerSetupTest.SMTP);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void receiveNotificationsAndSendMail() throws IOException, MessagingException {
        // dataset
        var notification1 = new Notification(null, "Housagotchi",
            LocalDateTime.of(2018, 4, 29, 10, 0), "notification 1", "hurry up!",
            new NotificationAction("http://www.stijnhooft.be", "Open it up", "internal"), PublishStrategy.PUBLISH_IMMEDIATELY);
        var notification2 = new Notification(null, "Article writer",
            LocalDateTime.of(2018, 4, 29, 11, 13), "notification 2", "chill...",
            new NotificationAction("http://portal.stijnhooft.be", "Do something about it", "internal"), PublishStrategy.PUBLISH_IMMEDIATELY);
        List<Notification> notifications = Arrays.asList(notification1, notification2);

        // execute
        sendToTopic("notificationTopic", notifications);

        //assert
        assertEmailContains("/expected/expected-notifications-mail.html");
    }

    @Test
    public void receiveActivitiesAndSendMail() throws IOException, MessagingException {
        // dataset
        var event1 = new Event(
            "Activity",
            "1",
            FlowAction.START,
            LocalDateTime.of(2018, 4, 29, 10, 0),
            Map.of(
                "photo", "pic.png",
                "name", "fun",
                "description", "really!"
            )
        );
        var event2 = new Event(
            "Activity",
            "2",
            FlowAction.START,
            LocalDateTime.of(2018, 4, 29, 10, 0),
            Map.of(
                "photo", "fjord.png",
                "name", "travel",
                "description", "to Norway!"
            )
        );
        var event3 = new Event(
            "Housagotchi",
            "3",
            FlowAction.START,
            LocalDateTime.of(2018, 4, 29, 10, 0),
            Map.of("", "")
        );
        List<Event> events = Arrays.asList(event1, event2, event3);

        // execute
        sendToTopic("eventTopic", events);

        //assert
        assertEmailContains("/expected/expected-activities-mail.html");
    }

    private void sendToTopic(String topic, Object contents) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(contents);
        var message = MessageBuilder.withBody(json.getBytes())
            .setContentType(MessageProperties.CONTENT_TYPE_JSON)
            .build();
        rabbitTemplate.send(topic, topic, message);
    }

    private void assertEmailContains(String pathToFileWithExpectedContents) throws IOException, MessagingException {
        await().untilAsserted(() -> assertThat(smtpServer.getReceivedMessages()).hasSize(1));

        try (var inputStream = requireNonNull(getClass().getResourceAsStream(pathToFileWithExpectedContents), "Could not find expected file " + pathToFileWithExpectedContents)) {
            String expected = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            String actual = (String) smtpServer.getReceivedMessages()[0].getContent();

            assertThat(actual.replaceAll("\\r\\n?", "\n").trim()).isEqualTo(expected.replaceAll("\\r\\n?", "\n").trim());
        }

    }
}
