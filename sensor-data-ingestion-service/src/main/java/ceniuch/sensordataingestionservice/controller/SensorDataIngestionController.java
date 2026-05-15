package ceniuch.sensordataingestionservice.controller;

import ceniuch.sensordataingestionservice.models.SensorData;
import ceniuch.sensordataingestionservice.dtos.SensorDataResponseDto;
import ceniuch.sensordataingestionservice.models.SensorRequest;
import ceniuch.sensordataingestionservice.service.SensorDataIngestionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class SensorDataIngestionController {

    private final SensorDataIngestionService sensorDataIngestionService;

    @Autowired
    public SensorDataIngestionController(SensorDataIngestionService sensorDataIngestionService) {
        this.sensorDataIngestionService = sensorDataIngestionService;
    }


    @PostMapping(value = "/api/sensor-data", consumes = "application/json", produces = "application/json")
    public ResponseEntity<SensorDataResponseDto> ingestionSensorData(
            @Valid @RequestBody SensorData sensorData,
            @RequestHeader(value = "X-SDS-API-Key", required = true) String apiKey,
            @RequestHeader(value = "X-Forwarded-For", required = true) String xForwardedFor
            ) {
        log.info("Received sensor data from sensor: {}, with apiKey: {}, xForwardedFor: {}",
                sensorData.sensorId(), apiKey, xForwardedFor);

        SensorRequest sensorRequest = new SensorRequest(apiKey, xForwardedFor, sensorData);

        SensorDataResponseDto response = sensorDataIngestionService.ingest(sensorRequest);
        return ResponseEntity.accepted().body(response);
    }
}
