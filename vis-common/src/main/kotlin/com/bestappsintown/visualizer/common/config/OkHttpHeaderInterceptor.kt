package com.bestappsintown.visualizer.common.config

import okhttp3.Interceptor
import okhttp3.Response
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.getBeansWithAnnotation
import org.springframework.context.ApplicationContext

class OkHttpHeaderInterceptor constructor(private val applicationContext: ApplicationContext) : Interceptor {

    @Value("\${server.port:error}")
    private lateinit var serverPort: String

    override fun intercept(chain: Interceptor.Chain): Response {
        return Response.Builder()
            .request(chain.request())
            .addHeader("MS-Port", serverPort)
            .addHeader("MS-Name", getApplicationName())
            .build()
    }

    fun getApplicationName(): String {
        var applicationName: String = applicationContext.applicationName;

        if (applicationName.isEmpty()) {
            val classes: Map<String, Any> = applicationContext.getBeansWithAnnotation<VisualizeServiceClient>()
            val mainClass: Map.Entry<String, Any> = classes.entries.stream().findFirst().get()
            val visualizeServiceClientAnnotation: VisualizeServiceClient =
                mainClass.value.javaClass.annotations.filter { t -> t.annotationClass == VisualizeServiceClient::class }
                    .first() as VisualizeServiceClient
            applicationName = visualizeServiceClientAnnotation.applicationName
        }

        return applicationName
    }
}