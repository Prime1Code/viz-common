package com.bestappsintown.viz.common.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.getBeansWithAnnotation
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class ApplicationUtils @Autowired constructor(
    private val applicationContext: ApplicationContext
){
    fun getApplicationName() : String {
        val classes: Map<String, Any> = applicationContext.getBeansWithAnnotation<VizClient>()
        val mainClass: Map.Entry<String, Any> = classes.entries.stream().findFirst().get()
        val vizClientAnnotation: VizClient =
            mainClass.value.javaClass.annotations.first { t ->
                t.annotationClass == VizClient::class
            } as VizClient
        return vizClientAnnotation.applicationName
    }
}