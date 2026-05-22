package com.ceniuch.sensordataingestionservice.controller;

import com.ceniuch.sensordataingestionservice.dtos.SensorDataResponseDto;
import com.ceniuch.sensordataingestionservice.models.SensorData;
import com.ceniuch.sensordataingestionservice.models.SensorRequest;
import com.ceniuch.sensordataingestionservice.service.SensorDataIngestionService;
import com.ceniuch.common.exceptions.SensorUnauthorizedException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/sensors")
@Slf4j
public class SensorDataIngestionController {

    private final SensorDataIngestionService sensorDataIngestionService;

    public SensorDataIngestionController(SensorDataIngestionService sensorDataIngestionService) {
        this.sensorDataIngestionService = sensorDataIngestionService;

        log.info("{}", Instant.now().toString());

    }

    @PostMapping(value = "/data", consumes = "application/json", produces = "application/json")
    public ResponseEntity<SensorDataResponseDto> ingestSensorData(
            @Valid @RequestBody SensorData sensorData,
            @RequestHeader(value = "X-SDS-API-Key") String apiKey,
            @RequestHeader(value = "X-Forwarded-For", required = false) String xForwardedFor
    ) {
        log.info("Received sensor data from sensor {} (forwardedFor={})", sensorData.sensorId(), xForwardedFor);

        SensorRequest sensorRequest = new SensorRequest(apiKey, xForwardedFor, sensorData);

        try {
            SensorDataResponseDto response = sensorDataIngestionService.ingest(sensorRequest);
            return ResponseEntity.accepted().body(response);

        } catch (SensorUnauthorizedException e) {
            log.error("Sensor Unauthorized Exception.", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


    }
}
