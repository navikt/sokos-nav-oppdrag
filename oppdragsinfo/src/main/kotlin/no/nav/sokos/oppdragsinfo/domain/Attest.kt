package no.nav.sokos.oppdragsinfo.domain

import kotlinx.serialization.Serializable

@Serializable
data class Attest(
    val attestantId: String,
    val ugyldigFom: String,
)
