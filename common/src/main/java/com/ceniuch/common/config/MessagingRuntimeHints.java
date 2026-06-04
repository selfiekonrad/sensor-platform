package com.ceniuch.common.config;

import com.ceniuch.common.events.SensorDataEvent;
import org.springframework.aot.hint.BindingReflectionHintsRegistrar;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

/**
 * Registers reflection hints so Jackson can (de)serialize the RabbitMQ event
 * payload in a native image.
 *
 * <p>The producer publishes via {@code RabbitTemplate.convertAndSend(Object)} and
 * the consumer receives it as a {@code @RabbitListener} parameter. Neither gives
 * Spring AOT enough static type information to register {@link SensorDataEvent}'s
 * accessors automatically, so without these hints Jackson serializes an empty
 * object and every field arrives {@code null} on the consumer.
 */
public class MessagingRuntimeHints implements RuntimeHintsRegistrar {

    private final BindingReflectionHintsRegistrar bindingRegistrar = new BindingReflectionHintsRegistrar();

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        // Transitively covers SensorDataEvent plus its property types
        // (UUID, Instant, Float, and the SensorType / Unit enums).
        bindingRegistrar.registerReflectionHints(hints.reflection(), SensorDataEvent.class);
    }
}