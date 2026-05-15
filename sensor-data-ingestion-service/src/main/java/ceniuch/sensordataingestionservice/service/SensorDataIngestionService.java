package ceniuch.sensordataingestionservice.service;

import ceniuch.sensordataingestionservice.config.RabbitMQConfig;
import ceniuch.sensordataingestionservice.dtos.SensorDataResponseDto;
import ceniuch.sensordataingestionservice.dtos.mappers.SensorDataMapper;
import ceniuch.sensordataingestionservice.models.SensorRequest;
import com.ceniuch.common.events.SensorDataEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
public class SensorDataIngestionService {

    private final SensorDataMapper sensorDataMapper;
    private final RabbitTemplate rabbitTemplate;

    public SensorDataIngestionService(SensorDataMapper sensorDataMapper, RabbitTemplate rabbitTemplate) {
        this.sensorDataMapper = sensorDataMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    public SensorDataResponseDto ingest(SensorRequest request) {
        SensorDataEvent event = sensorDataMapper.toEvent(request);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.SENSOR_EXCHANGE,
                "sensor.data.ingestion",
                event
        );

        log.debug("Enqueued sensor event {} for sensor {}", event.getEventId(), event.getSensorId());

        return new SensorDataResponseDto(
                "ACCEPTED",
                event.getEventId().toString(),
                "Data queued for processing",
                Instant.now()
        );
    }
}
