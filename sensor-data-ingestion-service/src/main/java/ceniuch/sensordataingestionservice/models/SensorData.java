package ceniuch.sensordataingestionservice.models;

import com.ceniuch.common.Unit;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record SensorData(
        @NotNull
        UUID sensorId,

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
