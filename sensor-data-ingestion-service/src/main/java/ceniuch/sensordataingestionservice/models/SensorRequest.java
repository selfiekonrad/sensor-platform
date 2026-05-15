package ceniuch.sensordataingestionservice.models;

public record SensorRequest(
        String apiKey,
        String xForwardedFor,
        SensorData sensorData
) {
}
