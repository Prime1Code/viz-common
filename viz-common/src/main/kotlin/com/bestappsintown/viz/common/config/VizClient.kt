package com.bestappsintown.viz.common.config

import org.springframework.context.annotation.Import
import java.lang.annotation.Inherited

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@Import(VizClientMarkerConfiguration::class)
annotation class VizClient(val applicationName: String)