package ceniuch.sensordataprocessingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.ceniuch.db.model")
//@EnableJpaRepositories("ceniuch.sensordataprocessingservice.repository")
public class SensorDataProcessingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SensorDataProcessingServiceApplication.class, args);
    }

}
