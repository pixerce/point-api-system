package com.point.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

import static com.point.system.common.Constants.ASIA_SEOUL;

@Configuration
public class BeanConfiguration {

    @Bean
    public Clock currentZoneClock() {
        return Clock.system(ZoneId.of(ASIA_SEOUL));
    }
}
