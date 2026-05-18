package ceniuch.sensordataingestionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
        exclude = {
                DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class
        }
)
@EnableJpaRepositories(basePackages = {
        "com.ceniuch.common.db",
        "ceniuch.sensordataingestionservice.repository"
})
@EntityScan(basePackages = {
        "com.ceniuch.db.model"
})
public class SensorDataIngestionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SensorDataIngestionServiceApplication.class, args);
    }

}
