package com.ceniuch.sensormanagementservice.service;

import com.ceniuch.common.db.SensorRepository;
import com.ceniuch.common.encryption.EncryptionService;
import com.ceniuch.db.model.Sensor;
import com.ceniuch.sensormanagementservice.dto.AlertDto;
import com.ceniuch.sensormanagementservice.dto.SensorReadingDto;
import com.ceniuch.sensormanagementservice.dto.SensorRegistryResponseDto;
import com.ceniuch.sensormanagementservice.model.SensorRegistryData;
import com.ceniuch.sensormanagementservice.repository.AlertRepository;
import com.ceniuch.sensormanagementservice.repository.SensorReadingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SensorQueryService {

    private final SensorReadingRepository readingRepository;
    private final AlertRepository alertRepository;
    private final EncryptionService encryptionService;
    private final SensorRepository sensorRepository;

    public SensorRegistryResponseDto registerSensor(SensorRegistryData sensorRegistryData) {

        Sensor sensor = new Sensor();
        sensor.setSensorType(sensorRegistryData.type());
        sensor.setName(sensorRegistryData.name());
        sensor.setApiKey(encryptionService.encryptKeyForDb(generateApiKey()));
        sensor.setCreatedAt(Instant.now());

        sensorRepository.save(sensor);

        return new SensorRegistryResponseDto(
                sensor.getId(), encryptionService.decryptKeyFromDb(sensor.getApiKey()), sensor.getCreatedAt()
        );
    }

    private String generateApiKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
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
