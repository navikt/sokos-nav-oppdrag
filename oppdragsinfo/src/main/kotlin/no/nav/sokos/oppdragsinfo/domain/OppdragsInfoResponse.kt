package no.nav.sokos.oppdragsinfo.domain

import kotlinx.serialization.Serializable

@Serializable
data class OppdragsInfoResponse(
    val gjelderId: String,
    val gjelderNavn: String? = null,
    val oppdrag : OppdragsDetaljer? = null
)