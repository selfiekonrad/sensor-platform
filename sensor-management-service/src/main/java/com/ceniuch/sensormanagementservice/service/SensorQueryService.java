package com.ceniuch.sensormanagementservice.service;

import com.ceniuch.sensormanagementservice.dto.AlertDto;
import com.ceniuch.sensormanagementservice.dto.SensorReadingDto;
import com.ceniuch.sensormanagementservice.repository.AlertRepository;
import com.ceniuch.sensormanagementservice.repository.SensorReadingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class SensorQueryService {

    private final SensorReadingRepository readingRepository;
    private final AlertRepository alertRepository;

    public SensorQueryService(SensorReadingRepository readingRepository, AlertRepository alertRepository) {
        this.readingRepository = readingRepository;
        this.alertRepository = alertRepository;
    }

    public Optional<SensorReadingDto> getCurrent(UUID sensorId) {
        return readingRepository.findTopBySensorIdOrderByTimestampDesc(sensorId)
                .map(SensorReadingDto::from);
    }

    public List<SensorReadingDto> getHistory(UUID sensorId, Instant from, Instant to) {
        return readingRepository
                .findBySensorIdAndTimestampBetweenOrderByTimestampAsc(sensorId, from, to)
                .stream()
                .map(SensorReadingDto::from)
                .toList();
    }

    public List<AlertDto> getActiveAlerts() {
        return alertRepository.findByResolvedFalseOrderByCreatedAtDesc()
                .stream()
                .map(AlertDto::from)
                .toList();
    }
}
