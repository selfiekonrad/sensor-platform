package com.ceniuch.sensormanagementservice.repository;


import com.ceniuch.db.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, Long> {
}
