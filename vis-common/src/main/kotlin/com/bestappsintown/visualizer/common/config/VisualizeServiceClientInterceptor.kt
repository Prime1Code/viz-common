package com.bestappsintown.visualizer.common.config

import com.bestappsintown.visualizer.common.dto.Ping
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.getBeansWithAnnotation
import org.springframework.context.ApplicationContext
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.io.IOException
import java.net.URI
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class VisualizeServiceClientInterceptor @Autowired constructor(
    private val applicationContext: ApplicationContext
) : HandlerInterceptor, InitializingBean {

    @Value("\${vsc.vssPort}")
    private lateinit var vssPort: String

    private val client = OkHttpClient()

    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?
    ) {
        try {
            val method = handler as HandlerMethod
            var msPort: String? = request.getHeader("MS-Port")
            var msName: String? = request.getHeader("MS-Name")
            var applicationName: String = applicationContext.applicationName;

            if (applicationName.isEmpty()) {
                val classes: Map<String, Any> = applicationContext.getBeansWithAnnotation<VisualizeServiceClient>()
                val mainClass: Map.Entry<String, Any> = classes.entries.stream().findFirst().get()
                val visualizeServiceClientAnnotation: VisualizeServiceClient =
                    mainClass.value.javaClass.annotations.filter { t -> t.annotationClass == VisualizeServiceClient::class }
                        .first() as VisualizeServiceClient
                applicationName = visualizeServiceClientAnnotation.applicationName
            }

            val ping = Ping(
                msName,
                request.remoteHost,
                request.remoteAddr,
                msPort ?: request.remotePort.toString(),
                applicationName,
                request.localName,
                request.localAddr,
                request.localPort.toString(),
                URI.create(request.requestURI),
                method.method.name,
                Date(),
                response.status.toString(),
            )

            val jsonBody = jacksonObjectMapper().writeValueAsString(ping)

            val request = Request.Builder()
                .url("http://localhost:$vssPort/ping/")
                .post(jsonBody.toRequestBody(MEDIA_TYPE_JSON))
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    }
                }
            })
        } catch (e: ClassCastException) {
        }
    }

    companion object {
        val MEDIA_TYPE_JSON = APPLICATION_JSON_VALUE.toMediaType()
    }

    override fun afterPropertiesSet() {
        if (!StringUtils.hasText(vssPort)) {
            throw IllegalArgumentException("vssPort is null")
        }
    }
}
