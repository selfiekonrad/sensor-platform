package ceniuch.sensordataingestionservice.auth;

import com.ceniuch.common.exceptions.SensorUnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class SensorAuthClient {

    private final RestClient restClient;

    public SensorAuthClient(@Value("${management-service.base-url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public void validate(UUID sensorId, String apiKey) throws SensorUnauthorizedException {
        HttpStatusCode status = restClient.post()
                .uri("/api/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("sensorId", sensorId, "apiKey", apiKey))
                .exchange((_, res) -> res.getStatusCode(), false);

        if (status.is2xxSuccessful()) {
            return;
        }
        if (status.value() == 401) {
            throw SensorUnauthorizedException.invalidApiKey();
        }
        log.error("Unexpected status {} from management /api/validate for sensor {}", status, sensorId);
        throw new IllegalStateException("Validation call failed with status " + status);
    }
}