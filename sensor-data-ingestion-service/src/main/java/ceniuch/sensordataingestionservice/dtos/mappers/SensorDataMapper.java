package ceniuch.sensordataingestionservice.dtos.mappers;

import ceniuch.sensordataingestionservice.models.SensorData;
import ceniuch.sensordataingestionservice.models.SensorDataEvent;
import org.springframework.stereotype.Component;

@Component
public class SensorDataMapper {
    public SensorDataEvent toEvent(SensorData dto) {
        SensorDataEvent sensorDataEvent = new SensorDataEvent();
        sensorDataEvent.setSensorId(dto.sensorId());
        sensorDataEvent.setValue(dto.value());
        sensorDataEvent.setUnit(dto.unit());
        sensorDataEvent.setTimestamp(dto.timestamp());
        return sensorDataEvent;
    }
}
