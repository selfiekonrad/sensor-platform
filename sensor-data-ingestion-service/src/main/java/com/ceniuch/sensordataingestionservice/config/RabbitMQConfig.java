package com.ceniuch.sensordataingestionservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String SENSOR_QUEUE = "sensor-data-queue";
    public static final String SENSOR_EXCHANGE = "sensor-exchange";
    public static final String SENSOR_ROUTING_KEY = "sensor.data.*";

    @Bean
    public Queue sensorQueue() {
        return new Queue(SENSOR_QUEUE, true);
    }

    @Bean
    public TopicExchange sensorExchange() {
        return new TopicExchange(SENSOR_EXCHANGE, true, false);
    }

    @Bean
    public Binding binding(Queue sensorQueue, TopicExchange sensorExchange) {
        return BindingBuilder.bind(sensorQueue)
                .to(sensorExchange)
                .with(SENSOR_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter(
                "com.ceniuch.common.*",
                "java.time.*",
                "java.util.*",
                "java.lang.*"
        );
    }
}
