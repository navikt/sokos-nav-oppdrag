package no.nav.sokos.app.config

import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.callid.callIdMdc
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.path
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.binder.system.UptimeMetrics
import java.util.UUID
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import no.nav.sokos.app.metrics.prometheusMeterRegistryApp
import no.nav.sokos.oppdragsinfo.config.oppdragsInfoRequestValidationConfig
import no.nav.sokos.oppdragsinfo.config.oppdragsInfoStatusPageConfig
import org.slf4j.event.Level

private const val SECURE_LOGGER = "secureLogger"
private const val AUDIT_LOGGER = "auditLogger"
val logger = KotlinLogging.logger {}

fun Application.commonConfig() {
    install(CallId) {
        header(HttpHeaders.XCorrelationId)
        generate { UUID.randomUUID().toString() }
        verify { callId: String -> callId.isNotEmpty() }
    }
    install(CallLogging) {
        level = Level.INFO
        callIdMdc(HttpHeaders.XCorrelationId)
        filter { call -> call.request.path().startsWith("/api") }
        disableDefaultColors()
    }
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true

            @OptIn(ExperimentalSerializationApi::class)
            explicitNulls = false

        })
    }
    install(StatusPages) {
        oppdragsInfoStatusPageConfig()
    }
    /*install(RequestValidation) {
        oppdragsInfoRequestValidationConfig()
    }*/
    install(MicrometerMetrics) {
        registry = prometheusMeterRegistryApp
        meterBinders = listOf(
            UptimeMetrics(),
            JvmMemoryMetrics(),
            JvmGcMetrics(),
            JvmThreadMetrics(),
            ProcessorMetrics()
        )
    }
}