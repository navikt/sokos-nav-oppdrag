package no.nav.sokos.oppdragsinfo.service

import io.ktor.server.application.ApplicationCall
import no.nav.sokos.oppdragsinfo.audit.AuditLogg
import no.nav.sokos.oppdragsinfo.audit.AuditLogger
import no.nav.sokos.oppdragsinfo.audit.Saksbehandler
import no.nav.sokos.oppdragsinfo.config.logger
import no.nav.sokos.oppdragsinfo.config.secureLogger
import no.nav.sokos.oppdragsinfo.database.Db2DataSource
import no.nav.sokos.oppdragsinfo.database.OppdragsInfoRepository.getOppdrag
import no.nav.sokos.oppdragsinfo.database.OppdragsInfoRepository.getOppdragsInfo
import no.nav.sokos.oppdragsinfo.database.RepositoryExtensions.setAcceleration
import no.nav.sokos.oppdragsinfo.database.RepositoryExtensions.useAndHandleErrors
import no.nav.sokos.oppdragsinfo.database.hentOppdrag
import no.nav.sokos.oppdragsinfo.database.hentOppdragsLinjer
import no.nav.sokos.oppdragsinfo.database.hentOppdragsenheter
import no.nav.sokos.oppdragsinfo.domain.OppdragsDetaljer
import no.nav.sokos.oppdragsinfo.domain.OppdragsInfo
import no.nav.sokos.oppdragsinfo.security.getSaksbehandler

class OppdragsInfoService(
    private val db2DataSource: Db2DataSource = Db2DataSource(),
    private val auditLogger: AuditLogger = AuditLogger()
) {

    fun sokOppdrag(
        gjelderId: String,
        applicationCall: ApplicationCall
    ): List<OppdragsInfo> {
        val saksbehandler = hentSaksbehandler(applicationCall)
        logger.info(
            "Søker etter oppdrag med gjelderId: $gjelderId"
        )
        secureLogger.info("Søker etter oppdrag med gjelderId: $gjelderId")
        auditLogger.auditLog(
            AuditLogg(
                saksbehandler = saksbehandler.ident,
                oppdragsId = gjelderId
            )
        )

        // TODO: Gjøre en sjekk på om gjelderId finnes eller ikke??
        val oppdragsInfo = db2DataSource.connection.useAndHandleErrors {
            it.setAcceleration(); it.getOppdragsInfo(gjelderId).firstOrNull()!!
        }
        val oppdrag =
            db2DataSource.connection.useAndHandleErrors { it.setAcceleration(); it.getOppdrag(oppdragsInfo.gjelderId) }

        return listOf(
            OppdragsInfo(
                gjelderId = oppdragsInfo.gjelderId,
                gjelderNavn = oppdragsInfo.gjelderNavn,
                oppdrag = oppdrag
            )
        )


    }

    /*    suspend fun hentOppdragslinje(
            oppdragsId: String,
            oppdragslinje: String,
            applicationCall: ApplicationCall
        ): List<OppdragslinjeVO> {
            val saksbehandler = hentSaksbehandler(applicationCall)
            secureLogger.info("Henter oppdrag med id: $oppdragsId")
            auditLogger.auditLog(
                AuditLogg(
                    saksbehandler = saksbehandler.ident,
                    oppdragsId = oppdragsId
                )
            )
            return db2DataSource.connection.useAndHandleErrors { connection ->
                connection.setAcceleration()
                val oppdragsLinjeInfo =
                    finnOppdragslinjeInfo(connection, oppdragsId.trim().toInt(), oppdragslinje.trim().toInt())

                oppdragsLinjeInfo.oppdragslinjer.map {
                    OppdragslinjeVO(
                        it.oppdragsId,
                        it.linjeId,
                        it.delytelseId,
                        it.sats,
                        it.typeSats,
                        it.vedtakFom.orEmpty(),
                        it.vedtakTom.orEmpty(),
                        it.attestert,
                        it.vedtaksId,
                        it.utbetalesTilId,
                        it.refunderesOrgnr.orEmpty(),
                        it.brukerid,
                        it.tidspktReg,
                        oppdragsLinjeInfo.skyldnere,
                        oppdragsLinjeInfo.valutaer,
                        oppdragsLinjeInfo.linjeenheter,
                        oppdragsLinjeInfo.kidliste,
                        oppdragsLinjeInfo.tekster,
                        oppdragsLinjeInfo.grader,
                        oppdragsLinjeInfo.kravhavere,
                        oppdragsLinjeInfo.maksdatoer
                    )
                }
            }
        } */

    /*    suspend fun finnOppdragslinjeInfo(connection: Connection, oppdragsId: Int, linjeId: Int): OppdragsLinjeInfo {
            val oppdLinje: OppdragsLinjeInfo
            coroutineScope {
                oppdLinje = OppdragsLinjeInfo(
                    async { connection.hentOppdragslinje(oppdragsId, linjeId) }.await(),
                    async { connection.hentSkyldnere(oppdragsId, linjeId) }.await(),
                    async { connection.hentValutaer(oppdragsId, linjeId) }.await(),
                    async { connection.hentLinjeenheter(oppdragsId, linjeId) }.await(),
                    async { connection.hentKidlister(oppdragsId, linjeId) }.await(),
                    async { connection.henOppdragsTekster(oppdragsId, linjeId) }.await(),
                    async { connection.hentGrader(oppdragsId, linjeId) }.await(),
                    async { connection.henKravhavere(oppdragsId, linjeId) }.await(),
                    async { connection.henMaksdatoer(oppdragsId, linjeId) }.await()
                )
            }
            return oppdLinje
        }*/

    suspend fun hentOppdrag(
        oppdragsId: String,
        applicationCall: ApplicationCall
    ): OppdragsDetaljer {
        val saksbehandler = hentSaksbehandler(applicationCall)
        secureLogger.info("Henter oppdrag med id: $oppdragsId")
        auditLogger.auditLog(
            AuditLogg(
                saksbehandler = saksbehandler.ident,
                oppdragsId = oppdragsId
            )
        )
        return db2DataSource.connection.useAndHandleErrors { connection ->
            connection.setAcceleration()
            val oppdragsinfo = connection.hentOppdrag(oppdragsId.trim().toInt()).first()
            val oppdragsenheter = connection.hentOppdragsenheter(oppdragsId.trim().toInt())
            val oppdragslinjer = connection.hentOppdragsLinjer(oppdragsId.trim().toInt())
            OppdragsDetaljer(
                oppdragsinfo.fagsystemId,
                oppdragsinfo.oppdragsId,
                oppdragsinfo.kjøresIdag,
                oppdragsinfo.fagOmraadeNavn,
                oppdragsinfo.status,
                oppdragsenheter,
                oppdragslinjer
            )
        }
    }

    private fun hentSaksbehandler(call: ApplicationCall): Saksbehandler {
        return getSaksbehandler(call)
    }
}