# portal-email
[![Build Status](https://server.stijnhooft.be/jenkins/buildStatus/icon?job=portal-email/master)](https://server.stijnhooft.be/jenkins/job/portal-email/job/master/)

This module transforms incoming messages to an email and sends them to the user.

## How to send an email with this project?
1. Send messages to a topic, for example the generic event topic.
1. Create or extend a topic listener to receive the message
1. Transform your message into a mail using a MailBuilder
1. Use the JavaMailSender to send the mail

## Structure of the project
The app is split into **packages per subject**. 
* If your messages arrive at the **(generic) events** topic, the classes in the events package pick up these messages and pass them to the correct service. These services are defined in their own package, per subject.
  * For example: an event from the Activity microservice arrives at the event topic.
    * It gets picked up in the events package.
    * Then, it gets passed to the services in the activities package.
    

* If you have a specific, **separate queue/topic for your messages**, create a new package which reads from this queue and sends the mail
  * For example: notifications arrive at the notifications topic. 
    * The code for receiving the message and sending the mail is put in the notifications package.

## SMTP settings
To configure your own smtp server, provide them in the *.env* file, next to docker-compose.yml.
Check out [How to run this suite of micro services](https://github.com/stainii/portal#how-to-run-this-suite-of-micro-services)

## Environment variables
| Name | Example value | Description | Required? |
| ---- | ------------- | ----------- | -------- |
| EMAIL_RECIPIENT | myemail@example.com | The email address of the receiver of the emails | required |
| EMAIL_SENDER | mysender@example.com | The email address that's used to send emails | required |
| EMAIL_HOST | smtp.gmail.com | The hostname of the SMTP server | required |
| EMAIL_PORT | 992 | The port of the SMTP server | required
| EMAIL_USERNAME | stainii | The username used to authenticate at the SMTP server | required |
| EMAIL_PASSWORD | secret | The password user to authenticate at the SMTP server | required |
| EMAIL_SMTP_AUTH | true | Is authentication to the SMTP server required? | required |
| EMAIL_STARTTLS | true | Should TLS be used to communicate with the SMTP server? | required |
| RABBITMQ_HOST | portal-rabbitmq | The host of RabbitMQ | required
| RABBITMQ_PORT | 5672 | The port of RabbitMQ | required
| RABBITMQ_USERNAME | guest | The username for RabbitMQ | required
| RABBITMQ_PASSWORD | guest | The username for RabbitMQ | required
| JAVA_OPTS_EMAIL | -Xmx400m -Xms400m | Java opts you want to pass to the JVM | optional

### Release
#### How to release
To release a module, this project makes use of the JGitflow plugin and the Dockerfile-maven-plugin.

1. Make sure all changes have been committed and pushed to Github.
1. Switch to the dev branch.
1. Make sure that the dev branch has at least all commits that were made to the master branch
1. Make sure that your Maven has been set up correctly (see below)
1. Run `mvn jgitflow:release-start -Pproduction`.
1. Run `mvn jgitflow:release-finish -Pproduction`.
1. In Github, mark the release as latest release.
1. Congratulations, you have released both a Maven and a Docker build!

More information about the JGitflow plugin can be found [here](https://gist.github.com/lemiorhan/97b4f827c08aed58a9d8).

##### Maven configuration
At the moment, releases are made on a local machine. No Jenkins job has been made (yet).
Therefore, make sure you have the following config in your Maven `settings.xml`;

````$xml
<servers>
    <server>
        <id>docker.io</id>
        <username>your_username</username>
        <password>*************</password>
    </server>
    <server>
        <id>portal-nexus-releases</id>
        <username>your_username</username>
        <password>*************</password>
    </server>
</servers>
````
* docker.io points to the Docker Hub.
* portal-nexus-releases points to my personal Nexus (see `<distributionManagement>` in the project's `pom.xml`)
