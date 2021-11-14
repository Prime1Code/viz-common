package com.majaku.viz.common.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("vzc")
class VizClientProperties {
    private lateinit var vzsPort: String
    private lateinit var vzsUrl: String
}