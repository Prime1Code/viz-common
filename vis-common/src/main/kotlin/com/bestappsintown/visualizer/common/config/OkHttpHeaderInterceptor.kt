package com.bestappsintown.visualizer.common.config

import okhttp3.Interceptor
import okhttp3.Response
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.getBeansWithAnnotation
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

@Component
class OkHttpHeaderInterceptor @Autowired constructor(
    private val applicationContext: ApplicationContext
) : Interceptor, InitializingBean {

    @Value("\${server.port:error}")
    private lateinit var serverPort: String

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader("MS-Port", serverPort)
            .addHeader("MS-Name", getApplicationName())
            .build()
        return chain.proceed(newRequest)
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

    override fun afterPropertiesSet() {
        if (!StringUtils.hasText(serverPort)) {
            throw IllegalArgumentException("serverPort is null")
        }
    }
}