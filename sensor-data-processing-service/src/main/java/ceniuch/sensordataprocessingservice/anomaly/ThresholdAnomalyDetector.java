package ceniuch.sensordataprocessingservice.anomaly;

import com.ceniuch.db.model.Alert;
import com.ceniuch.db.model.AlertType;
import com.ceniuch.db.model.SensorReading;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class ThresholdAnomalyDetector {

    private static final float CELSIUS_HIGH = 80f;
    private static final float CELSIUS_LOW = -20f;
    private static final float BAR_HIGH = 10f;
    private static final float BAR_LOW = 0f;

    public Optional<Alert> check(SensorReading reading) {
        if (reading.getValue() == null || reading.getUnit() == null) {
            return Optional.empty();
        }

        return switch (reading.getUnit()) {
            case CELSIUS -> checkRange(reading, CELSIUS_LOW, CELSIUS_HIGH,
                    AlertType.TEMPERATURE_LOW, AlertType.TEMPERATURE_HIGH);
            case BAR, PASCAL -> checkRange(reading, BAR_LOW, BAR_HIGH,
                    AlertType.PRESSURE_LOW, AlertType.PRESSURE_HIGH);
            case PERCENT -> checkRange(reading, 0f, 100f,
                    AlertType.OUT_OF_RANGE, AlertType.OUT_OF_RANGE);
        };
    }

    private Optional<Alert> checkRange(
            SensorReading reading, float low, float high, AlertType lowType, AlertType highType) {

        float value = reading.getValue();
        if (value > high) {
            return Optional.of(buildAlert(reading, highType, high, value,
                    "Value %.2f exceeded upper threshold %.2f".formatted(value, high)));
        }
        if (value < low) {
            return Optional.of(buildAlert(reading, lowType, low, value,
                    "Value %.2f fell below lower threshold %.2f".formatted(value, low)));
        }
        return Optional.empty();
    }

    private Alert buildAlert(SensorReading reading, AlertType type, float threshold, float actual, String message) {
        Alert alert = new Alert();
        alert.setSensorId(reading.getSensorId());
        alert.setReadingId(reading.getId());
        alert.setAlertType(type);
        alert.setMessage(message);
        alert.setThresholdValue(threshold);
        alert.setActualValue(actual);
        alert.setUnit(reading.getUnit());
        alert.setCreatedAt(Instant.now());
        alert.setResolved(false);
        return alert;
    }
}
