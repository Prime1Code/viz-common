package com.bestappsintown.visualizer.common.config

import org.springframework.context.annotation.Import
import java.lang.annotation.Inherited

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@Import(VisualizeServiceClientMarkerConfiguration::class)
annotation class VisualizeServiceClient(val applicationName: String)
