package com.bestappsintown.visualizer.common.config

import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@Import(VisualizeServiceClientInterceptor::class, OkHttpHeaderInterceptor::class)
@EnableConfigurationProperties(VisualizeServiceClientProperties::class)
@ConditionalOnBean(VisualizeServiceClientMarkerConfiguration.Marker::class)
class VisualizeServiceClientInterceptorAutoConfiguration : WebMvcConfigurer {

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