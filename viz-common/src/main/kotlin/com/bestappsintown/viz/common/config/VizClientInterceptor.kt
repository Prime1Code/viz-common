package com.bestappsintown.viz.common.config

import com.bestappsintown.viz.common.dto.Ping
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
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
class VizClientInterceptor @Autowired constructor(
    private val applicationUtils: ApplicationUtils
) : HandlerInterceptor, InitializingBean {

    @Value("\${vzc.vzsPort}")
    private lateinit var vzsPort: String

    @Value("\${vzc.vzsUrl}")
    private lateinit var vzsUrl: String

    private val client = OkHttpClient()
    private val MEDIA_TYPE_JSON = APPLICATION_JSON_VALUE.toMediaType()

    private var applicationName: String? = null

    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?
    ) {
        try {
            val method = handler as HandlerMethod
            val msPort = request.getHeader("MS-Port")
            val msName = request.getHeader("MS-Name")

            if (!StringUtils.hasText(applicationName)) {
                applicationName = applicationUtils.getApplicationName()
            }

            val ping = Ping(
                msName,
                request.remoteHost,
                request.remoteAddr,
                msPort ?: request.remotePort.toString(),
                applicationName!!,
                request.localName,
                request.localAddr,
                request.localPort.toString(),
                URI.create(request.requestURI),
                method.method.name,
                Date(),
                response.status.toString(),
            )

            val jsonBody = jacksonObjectMapper().writeValueAsString(ping)

            val pingRequest = Request.Builder()
                .url("$vzsUrl:$vzsPort/ping/")
                .post(jsonBody.toRequestBody(MEDIA_TYPE_JSON))
                .build()

            client.newCall(pingRequest).enqueue(object : Callback {
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

    override fun afterPropertiesSet() {
        if (!StringUtils.hasText(vzsPort)) {
            throw IllegalArgumentException("vzsPort is null")
        }
        if (!StringUtils.hasText(vzsUrl)) {
            throw IllegalArgumentException("vzsUrl is null")
        }
    }
}
