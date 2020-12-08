package com.safetynet.alerts.config;

import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Config actuator
 */
@Configuration
public class ActuatorConfig {

    /**
     * Configure httpTraceRepository endpoints with actuator
     *
     * @return new InMemoryHttpTraceRepository
     */
    @Bean
    public HttpTraceRepository httpTraceRepository() {
        return new InMemoryHttpTraceRepository();
    }

}
