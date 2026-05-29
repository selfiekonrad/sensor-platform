package com.ceniuch.sensordataingestionservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class RestClientConfig {

    /**
     * Bounded connect/read timeouts so the synchronous auth call on the ingestion
     * hot path can never hang indefinitely when the management service is slow or
     * unreachable. A timeout surfaces as a {@code ResourceAccessException}, which
     * {@code SensorAuthClient} translates into a 503. Built on the JDK HttpClient
     * (no Boot-specific helpers) so it is stable across versions and GraalVM-safe.
     */
    @Bean
    public RestClient.Builder restClientBuilder() {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(2))
                .build();

        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(Duration.ofSeconds(3));

        return RestClient.builder().requestFactory(requestFactory);
    }
}
