package com.bestappsintown.viz.common.dto

import java.net.URI
import java.util.*

data class Ping(
    val srcServiceName: String?,
    val srcHostName: String,
    val srcHostAddr: String,
    val srcHostPort: String,

    val destServiceName: String,
    val destHostName: String,
    val destHostAddr: String,
    val destHostPort: String,
    val destUri: URI,
    val destEndpointMethod: String,

    val timestamp: Date,
    val responseStatus: String,
)
