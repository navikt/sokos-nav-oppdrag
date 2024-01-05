package no.nav.sokos.oppdragsinfo.domain

import kotlinx.serialization.Serializable

@Serializable
data class OppdragsLinje (
    val linjeId: Int,
    val sats: Double,
    val typeSats: String,
    val vedtakFom: String?,
    val vedtakTom: String?,
    val kodeKlasse: String?,
    val linjeIdKorreksjon: Int?,
    val attestert: String?,
    val status: String?
)