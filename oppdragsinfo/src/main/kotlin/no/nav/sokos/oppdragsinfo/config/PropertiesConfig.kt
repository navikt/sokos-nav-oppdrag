package no.nav.sokos.oppdragsinfo.config

import com.natpryce.konfig.ConfigurationMap
import com.natpryce.konfig.ConfigurationProperties
import com.natpryce.konfig.EnvironmentVariables
import com.natpryce.konfig.Key
import com.natpryce.konfig.overriding
import com.natpryce.konfig.stringType
import java.io.File

object PropertiesConfig {

    private val defaultProperties = ConfigurationMap(
        mapOf()
    )

    private val localDevProperties = ConfigurationMap(
        mapOf(
            "DATABASE_HOST" to "databaseHost",
            "DATABASE_PORT" to "databasePort",
            "DATABASE_NAME" to "databaseName",
            "DATABASE_SCHEMA" to "databaseSchema",
            "DATABASE_USERNAME" to "databaseUsername",
            "DATABASE_PASSWORD" to "databasePassword"
        )
    )

    private val devProperties = ConfigurationMap(mapOf("APPLICATION_PROFILE" to Profile.DEV.toString()))
    private val prodProperties = ConfigurationMap(mapOf("APPLICATION_PROFILE" to Profile.PROD.toString()))

    private val config = when (System.getenv("NAIS_CLUSTER_NAME") ?: System.getProperty("NAIS_CLUSTER_NAME")) {
        "dev-gcp" -> ConfigurationProperties.systemProperties() overriding EnvironmentVariables() overriding devProperties overriding defaultProperties
        "prod-gcp" -> ConfigurationProperties.systemProperties() overriding EnvironmentVariables() overriding prodProperties overriding defaultProperties
        else -> ConfigurationProperties.systemProperties() overriding EnvironmentVariables() overriding ConfigurationProperties.fromOptionalFile(
            File("defaults.properties")
        ) overriding localDevProperties overriding defaultProperties
    }

    operator fun get(key: String): String = config[Key(key, stringType)]
    data class OppdragDatabaseConfig(
        val host: String = get("DATABASE_HOST"),
        val port: String = get("DATABASE_PORT"),
        val name: String = get("DATABASE_NAME"),
        val schema: String = get("DATABASE_SCHEMA"),
        val username: String = get("DATABASE_USERNAME"),
        val password: String = get("DATABASE_PASSWORD"),
    )

    data class AzureAdClientConfig(
        val clientId: String = get("AZURE_APP_CLIENT_ID"),
        val wellKnownUrl: String = get("AZURE_APP_WELL_KNOWN_URL"),
        val tenantId: String = get("AZURE_APP_TENANT_ID"),
        val clientSecret: String = get("AZURE_APP_CLIENT_SECRET"),
    )

    data class PdlConfig(
        val pdlHost: String = get("PDL_HOST"),
        val pdlScope: String = get("PDL_SCOPE")
    )

    data class EksterneHost(
        val eregHost: String = get("EREG_HOST"),
        val tpHost: String = get("TP_HOST")
    )

    enum class Profile {
        DEV, PROD
    }
}