package com.ceniuch.sensormanagementservice.controller;

import com.ceniuch.sensormanagementservice.dto.AlertDto;
import com.ceniuch.sensormanagementservice.dto.SensorReadingDto;
import com.ceniuch.sensormanagementservice.service.SensorQueryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class SensorQueryController {

    private final SensorQueryService queryService;

    public SensorQueryController(SensorQueryService queryService) {
        this.queryService = queryService;
    }

    @PostMapping("register")
    public ResponseEntity<?> registerSensor() {

    }

    @GetMapping("/sensors/{id}/current")
    public ResponseEntity<SensorReadingDto> current(@PathVariable UUID id) {
        return queryService.getCurrent(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/sensors/{id}/history")
    public List<SensorReadingDto> history(
            @PathVariable UUID id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        return queryService.getHistory(id, from, to);
    }

    @GetMapping("/alerts")
    public List<AlertDto> alerts() {
        return queryService.getActiveAlerts();
    }
}
