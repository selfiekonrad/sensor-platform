package com.ceniuch.sensormanagementservice.model;

import java.util.UUID;

public record SensorRegistryData(
        String name,
        UUID Type
) {
}
