package ceniuch.sensordataingestionservice.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
public class SensorDataEvent implements Serializable {
    private UUID eventId;
    private UUID machineId;
    private UUID sensorId;
    private Float value;
    private Unit unit;
    private Instant timestamp;
    private Instant enqueuedAt;
}
