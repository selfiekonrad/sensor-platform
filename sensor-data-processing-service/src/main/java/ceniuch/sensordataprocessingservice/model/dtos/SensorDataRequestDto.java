package ceniuch.sensordataprocessingservice.model.dtos;

import jakarta.validation.constraints.NotNull;
import ceniuch.sensordataingestionservice.models.Unit;

import java.time.Instant;
import java.util.UUID;

public record SensorDataRequestDto(
        //@NotNull(message = "Sensor ID cannot be null")
        //@NotBlank(message = "Sensor ID cannot be blank")
        UUID sensorId,

        @NotNull
        UUID machineId,

        @NotNull
        UUID sensorType,

        @NotNull
        Float value,

        @NotNull
        Unit unit,

        @NotNull
        Instant timestamp
) {
}
