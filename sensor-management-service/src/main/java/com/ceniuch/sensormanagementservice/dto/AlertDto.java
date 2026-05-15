package com.ceniuch.sensormanagementservice.dto;

import com.ceniuch.common.Unit;
import com.ceniuch.db.model.Alert;
import com.ceniuch.db.model.AlertType;

import java.time.Instant;
import java.util.UUID;

public record AlertDto(
        UUID id,
        UUID sensorId,
        UUID readingId,
        AlertType alertType,
        String message,
        Float thresholdValue,
        Float actualValue,
        Unit unit,
        Instant createdAt,
        boolean resolved
) {
    public static AlertDto from(Alert alert) {
        return new AlertDto(
                alert.getId(),
                alert.getSensorId(),
                alert.getReadingId(),
                alert.getAlertType(),
                alert.getMessage(),
                alert.getThresholdValue(),
                alert.getActualValue(),
                alert.getUnit(),
                alert.getCreatedAt(),
                alert.isResolved()
        );
    }
}
