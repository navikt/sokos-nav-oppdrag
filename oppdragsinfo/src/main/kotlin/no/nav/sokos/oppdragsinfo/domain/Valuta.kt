package no.nav.sokos.oppdragsinfo.domain

import kotlinx.serialization.Serializable

@Serializable
data class Valuta(
    val linjeId: Int,
    val type: String,
    val datoFom: String,
    val nokkelId: Int,
    val valuta: String,
    val feilreg: String? = null,
    val tidspktReg: String,
    val brukerid: String
)
