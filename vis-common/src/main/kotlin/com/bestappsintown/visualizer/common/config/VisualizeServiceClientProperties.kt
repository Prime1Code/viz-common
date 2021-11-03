package com.bestappsintown.visualizer.common.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("vsc")
class VisualizeServiceClientProperties {
    private lateinit var vssPort: String
    private lateinit var vssUrl: String
}