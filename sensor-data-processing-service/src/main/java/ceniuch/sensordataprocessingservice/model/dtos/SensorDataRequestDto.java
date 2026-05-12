package ceniuch.sensordataprocessingservice.model.dtos;

import ceniuch.sensordataprocessingservice.model.Unit;
import jakarta.validation.constraints.NotNull;

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
