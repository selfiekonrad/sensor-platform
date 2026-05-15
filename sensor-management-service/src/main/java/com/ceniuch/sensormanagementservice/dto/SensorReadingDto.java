package com.ceniuch.sensormanagementservice.dto;

import com.ceniuch.common.Unit;
import com.ceniuch.db.model.SensorReading;

import java.time.Instant;
import java.util.UUID;

public record SensorReadingDto(
        UUID id,
        UUID sensorId,
        UUID sensorType,
        Float value,
        Unit unit,
        Instant timestamp,
        Instant ingestedAt
) {
    public static SensorReadingDto from(SensorReading reading) {
        return new SensorReadingDto(
                reading.getId(),
                reading.getSensorId(),
                reading.getSensorType(),
                reading.getValue(),
                reading.getUnit(),
                reading.getTimestamp(),
                reading.getIngestedAt()
        );
    }
}
