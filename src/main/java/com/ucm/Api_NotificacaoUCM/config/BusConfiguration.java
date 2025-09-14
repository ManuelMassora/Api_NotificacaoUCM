package com.ucm.Api_NotificacaoUCM.config;

import org.springframework.cloud.bus.BusBridge;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BusConfiguration {
    @Bean
    public BusBridge busBridge() {
        return new BusBridge() {
            @Override
            public void send(RemoteApplicationEvent event) {

            }
        };
    }
}
