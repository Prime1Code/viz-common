package com.majaku.viz.common.config

import okhttp3.Interceptor
import okhttp3.Response
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

@Component
class OkHttpHeaderInterceptor @Autowired constructor(
    private val applicationUtils: ApplicationUtils
) : Interceptor, InitializingBean {

    @Value("\${server.port:error}")
    private lateinit var serverPort: String

    private var applicationName: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!StringUtils.hasText(applicationName)) {
            applicationName = applicationUtils.getApplicationName()
        }
        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader("MS-Port", serverPort)
            .addHeader("MS-Name", applicationName!!)
            .build()
        return chain.proceed(newRequest)
    }

    override fun afterPropertiesSet() {
        if (!StringUtils.hasText(serverPort)) {
            throw IllegalArgumentException("serverPort is null")
        }
    }
}