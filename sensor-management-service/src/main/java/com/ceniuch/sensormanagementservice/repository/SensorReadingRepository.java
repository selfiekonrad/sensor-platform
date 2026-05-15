package com.ceniuch.sensormanagementservice.repository;

import com.ceniuch.db.model.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SensorReadingRepository extends JpaRepository<SensorReading, UUID> {

    Optional<SensorReading> findTopBySensorIdOrderByTimestampDesc(UUID sensorId);

    List<SensorReading> findBySensorIdAndTimestampBetweenOrderByTimestampAsc(
            UUID sensorId, Instant from, Instant to);
}
