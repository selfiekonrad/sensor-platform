package ceniuch.sensordataingestionservice.service;

import ceniuch.sensordataingestionservice.config.RabbitMQConfig;
import ceniuch.sensordataingestionservice.model.SensorDataEvent;
import ceniuch.sensordataingestionservice.model.SensorRequest;
import ceniuch.sensordataingestionservice.model.dtos.SensorData;
import ceniuch.sensordataingestionservice.model.dtos.SensorDataResponseDto;
import ceniuch.sensordataingestionservice.model.dtos.mappers.SensorDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
public class SensorDataIngestionService {
    private final SensorDataMapper sensorDataMapper;
    private final RabbitTemplate rabbitTemplate;

    public SensorDataIngestionService(SensorDataMapper sensorDataMapper, RabbitTemplate rabbitTemplate) {
        this.sensorDataMapper = sensorDataMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    public SensorDataResponseDto ingest(SensorRequest sensorRequest) {


        SensorDataEvent sensorDataEvent = sensorDataMapper.toEvent(sensorRequest.sensorData());
        sensorDataEvent.setEventId(UUID.randomUUID());
        sensorDataEvent.setEnqueuedAt(Instant.now());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.SENSOR_EXCHANGE,
                "sensor.data.ingestion",
                sensorDataEvent
        );

        log.info("Sensor data send: {}", sensorDataEvent);

        return new SensorDataResponseDto(
                "ACCEPTED",
                sensorDataEvent.getEventId().toString(),
                "Data queued for processing",
                Instant.now()
        );
    }

    // TODO: BatchIngest

    private void authenticateRequest(SensorRequest request) {

    }
}
