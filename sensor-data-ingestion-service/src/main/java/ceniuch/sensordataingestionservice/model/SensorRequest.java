package ceniuch.sensordataingestionservice.model;

import ceniuch.sensordataingestionservice.model.dtos.SensorData;

public record SensorRequest(
        String apiKey,
        String xForwardedFor,
        SensorData sensorData
) {
}
