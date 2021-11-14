package com.majaku.viz.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class VizClientMarkerConfiguration {

    @Bean
    fun visualizeServiceClientMarker(): Marker? {
        return Marker()
    }

    class Marker
}