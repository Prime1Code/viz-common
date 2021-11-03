package com.bestappsintown.visualizer.common.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("vsc")
class VisualizeServiceClientProperties {

    private lateinit var vssPort: String

    fun getVssPort(): String {
        return this.vssPort
    }

    fun setVssPort(port: String) {
        this.vssPort = port
    }
}