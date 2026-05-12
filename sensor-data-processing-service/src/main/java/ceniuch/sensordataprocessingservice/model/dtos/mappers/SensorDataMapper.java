package ceniuch.sensordataprocessingservice.model.dtos.mappers;

import ceniuch.sensordataprocessingservice.model.SensorDataEvent;
import ceniuch.sensordataprocessingservice.model.dtos.SensorDataRequestDto;
import org.springframework.stereotype.Component;

@Component
public class SensorDataMapper {
    public SensorDataEvent toEvent(SensorDataRequestDto dto) {
        SensorDataEvent sensorDataEvent = new SensorDataEvent();
        sensorDataEvent.setMachineId(dto.machineId());
        sensorDataEvent.setSensorId(dto.sensorId());
        sensorDataEvent.setValue(dto.value());
        sensorDataEvent.setUnit(dto.unit());
        sensorDataEvent.setTimestamp(dto.timestamp());
        return sensorDataEvent;
    }
}
