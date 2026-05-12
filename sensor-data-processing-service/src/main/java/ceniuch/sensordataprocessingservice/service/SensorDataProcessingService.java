package ceniuch.sensordataprocessingservice.service;

import ceniuch.sensordataprocessingservice.config.RabbitMQConfig;
import ceniuch.sensordataprocessingservice.model.SensorDataEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@RabbitListener(queues = RabbitMQConfig.SENSOR_QUEUE)
@Component
public class SensorDataProcessingService {

    @RabbitHandler
    void receive(SensorDataEvent sensorDataEvent) {
        log.info("received message: {}", sensorDataEvent.getEventId());
    }
}
