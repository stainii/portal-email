FROM openjdk:11.0.1-jdk-sid
VOLUME /tmp
EXPOSE 2004
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ENTRYPOINT exec java $JAVA_OPTS_PORTAL_NOTIFICATIONS_EMAIL_PLUGIN -Djava.security.egd=file:/dev/./urandom -jar /app.jar --mail.recipient=${EMAIL_RECIPIENT} --mail.sender=${EMAIL_SENDER} --mail.subject=${EMAIL_SUBJECT} --spring.mail.host=${EMAIL_HOST} --spring.mail.port=${EMAIL_PORT} --spring.mail.username=${EMAIL_USERNAME} --spring.mail.password=${EMAIL_PASSWORD} --spring.mail.properties.mail.smtp.auth=${EMAIL_SMTP_AUTH} --spring.mail.properties.mail.smtp.starttls.enable=${EMAIL_STARTTLS}
