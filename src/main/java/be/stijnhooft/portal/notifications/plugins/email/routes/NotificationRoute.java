package be.stijnhooft.portal.notifications.plugins.email.routes;

import be.stijnhooft.portal.notifications.model.Notification;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jackson.ListJacksonDataFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NotificationRoute extends RouteBuilder {

    @Value("${notifications.route.from}")
    private String fromRoute;

    @Override
    public void configure() throws Exception {
        JacksonDataFormat formatForListOfNotifications = new ListJacksonDataFormat(Notification.class);
        formatForListOfNotifications.addModule(new ParameterNamesModule());
        formatForListOfNotifications.addModule(new Jdk8Module());
        formatForListOfNotifications.addModule(new JavaTimeModule());
        formatForListOfNotifications.disableFeature(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        from(fromRoute)
            .unmarshal(formatForListOfNotifications)
            .to("bean:notificationService?method=receiveNotificationsAndSendMail(${body})")
            .id("notificationRoute");
    }

}
