package be.stijnhooft.portal.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class PortalEmailApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortalEmailApplication.class, args);
    }

}
