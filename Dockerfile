FROM eclipse-temurin:21-alpine
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ENTRYPOINT exec java $JAVA_OPTS_EMAIL -Djava.security.egd=file:/dev/./urandom -jar /app.jar \
    --mail.recipient=${EMAIL_RECIPIENT} \
    --mail.sender=${EMAIL_SENDER} \
    --spring.mail.host=${EMAIL_HOST} \
    --spring.mail.port=${EMAIL_PORT} \
    --spring.mail.username=${EMAIL_USERNAME} \
    --spring.mail.password=${EMAIL_PASSWORD} \
    --spring.mail.properties.mail.smtp.auth=${EMAIL_SMTP_AUTH} \
    --spring.mail.properties.mail.smtp.starttls.enable=${EMAIL_STARTTLS} \
    --eureka.client.service-url.defaultZone=${EUREKA_SERVICE_URL} \
    --spring.rabbitmq.host=${RABBITMQ_HOST} \
    --spring.rabbitmq.port=${RABBITMQ_PORT} \
    --spring.rabbitmq.username=${RABBITMQ_USERNAME} \
    --spring.rabbitmq.password=${RABBITMQ_PASSWORD}
