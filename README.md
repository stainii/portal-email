# portal-notifications-email-plugin
[![Build Status](https://server.stijnhooft.be/jenkins/buildStatus/icon?job=portal-notifications-email-plugin/master)](https://server.stijnhooft.be/jenkins/job/portal-notifications-email-plugin/job/master/)

Email plugin for the Notifications module. This module transforms incoming
        notifications to an email and send it to the user.

## SMTP settings
To configure your own smtp server, provide them in the *.env* file, next to docker-compose.yml.
Check out [How to run this suite of micro services](https://github.com/stainii/portal#how-to-run-this-suite-of-micro-services)

## Environment variables
| Name | Example value | Description | Required? |
| ---- | ------------- | ----------- | -------- |
| EMAIL_RECIPIENT | myemail@example.com | The email address of the receiver of the notifications | required |
| EMAIL_SENDER | mysender@example.com | The email address that's used to send notifications emails | required |
| EMAIL_SUBJECT | You've got a notification! | The subject of the notification email | required |
| EMAIL_HOST | smtp.gmail.com | The hostname of the SMTP server | required |
| EMAIL_PORT | 992 | The port of the SMTP server | required
| EMAIL_USERNAME | stainii | The username used to authenticate at the SMTP server | required |
| EMAIL_PASSWORD | secret | The password user to authenticate at the SMTP server | required |
| EMAIL_SMTP_AUTH | true | Is authentication to the SMTP server required? | required |
| EMAIL_STARTTLS | true | Should TLS be used to communicate with the SMTP server? | required |
| JAVA_OPTS_NOTIFICATIONS_EMAIL_PLUGIN | -Xmx400m -Xms400m | Java opts you want to pass to the JVM | optional

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
