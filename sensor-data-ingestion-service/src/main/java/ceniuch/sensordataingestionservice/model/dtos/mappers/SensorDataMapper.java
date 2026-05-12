package ceniuch.sensordataingestionservice.model.dtos.mappers;

import ceniuch.sensordataingestionservice.model.SensorDataEvent;
import ceniuch.sensordataingestionservice.model.dtos.SensorData;
import org.springframework.stereotype.Component;

@Component
public class SensorDataMapper {
    public SensorDataEvent toEvent(SensorData dto) {
        SensorDataEvent sensorDataEvent = new SensorDataEvent();
        sensorDataEvent.setMachineId(dto.machineId());
        sensorDataEvent.setSensorId(dto.sensorId());
        sensorDataEvent.setValue(dto.value());
        sensorDataEvent.setUnit(dto.unit());
        sensorDataEvent.setTimestamp(dto.timestamp());
        return sensorDataEvent;
    }
}
