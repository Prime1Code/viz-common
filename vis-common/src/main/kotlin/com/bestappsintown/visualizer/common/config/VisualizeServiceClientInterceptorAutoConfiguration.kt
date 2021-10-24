package com.bestappsintown.visualizer.common.config

import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(VisualizeServiceClientMarkerConfiguration.Marker::class)
class VisualizeServiceClientInterceptorAutoConfiguration : WebMvcConfigurerAdapter() {

    @Autowired
    lateinit var visualizeServiceClientInterceptor: VisualizeServiceClientInterceptor

    @Autowired
    lateinit var okHttpHeaderInterceptor: OkHttpHeaderInterceptor

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(visualizeServiceClientInterceptor)
    }

    @Bean(name = ["withInterceptor"])
    fun okHttpClientWithInterceptor () : OkHttpClient {
        return OkHttpClient()
            .newBuilder()
            .addInterceptor(okHttpHeaderInterceptor)
            .build()
    }
}