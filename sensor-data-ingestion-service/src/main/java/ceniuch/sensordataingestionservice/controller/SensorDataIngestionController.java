package ceniuch.sensordataingestionservice.controller;

import ceniuch.sensordataingestionservice.dtos.SensorDataResponseDto;
import ceniuch.sensordataingestionservice.models.SensorData;
import ceniuch.sensordataingestionservice.models.SensorRequest;
import ceniuch.sensordataingestionservice.service.SensorDataIngestionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/sensors")
@Slf4j
public class SensorDataIngestionController {

    private final SensorDataIngestionService sensorDataIngestionService;

    public SensorDataIngestionController(SensorDataIngestionService sensorDataIngestionService) {
        this.sensorDataIngestionService = sensorDataIngestionService;
    }

    @PostMapping(value = "/{sensorId}/data", consumes = "application/json", produces = "application/json")
    public ResponseEntity<SensorDataResponseDto> ingestSensorData(
            @Valid @RequestBody SensorData sensorData,
            @RequestHeader(value = "X-SDS-API-Key") String apiKey,
            @RequestHeader(value = "X-Forwarded-For", required = false) String xForwardedFor
    ) {
        log.info("Received sensor data from sensor {} (forwardedFor={})", sensorData.sensorId(), xForwardedFor);

        SensorRequest sensorRequest = new SensorRequest(apiKey, xForwardedFor, sensorData);
        SensorDataResponseDto response = sensorDataIngestionService.ingest(sensorRequest);
        return ResponseEntity.accepted().body(response);
    }
}
