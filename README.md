# portal-notifications-email-plugin
[![Build Status](http://portal-ci.westeurope.cloudapp.azure.com/buildStatus/icon?job=portal-notifications-email-plugin/master)](http://portal-ci.westeurope.cloudapp.azure.com/job/portal-notifications-email-plugin/job/master/)

Email plugin for the Notifications module. This module transforms incoming
        notifications to an email and send it to the user.

## SMTP settings
To configure your own smtp server, provide a file called *application.properties* in *src/main/resources/config/*.
This file should contain (at least) the same properties as *_example_application.properties_for_secrets*, which can be found in the same folder.
