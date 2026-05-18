package ceniuch.sensordataingestionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication()
@EnableJpaRepositories(basePackages = {
        "com.ceniuch.common.db"
})
@EntityScan(basePackages = {
        "com.ceniuch.db.model"
})
public class SensorDataIngestionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SensorDataIngestionServiceApplication.class, args);
    }

}
