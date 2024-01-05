package no.nav.sokos.oppdragsinfo.domain

import kotlinx.serialization.Serializable

@Serializable
data class OppdragsDetaljer (
    val fagsystemId: String,
    val oppdragsId: Int,
    val kjøresIdag: String,
    val fagOmraadeNavn: String,
    val status: String,
    val oppdragsEnheter: List<OppdragsEnhet>?,
    val oppdragsLinjer: List<OppdragsLinje>?
)