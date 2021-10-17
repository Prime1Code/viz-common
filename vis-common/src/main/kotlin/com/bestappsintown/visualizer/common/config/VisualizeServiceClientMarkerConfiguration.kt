package com.bestappsintown.visualizer.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class VisualizeServiceClientMarkerConfiguration {

    @Bean
    fun visualizeServiceClientMarker(): Marker? {
        return Marker()
    }

    class Marker
}