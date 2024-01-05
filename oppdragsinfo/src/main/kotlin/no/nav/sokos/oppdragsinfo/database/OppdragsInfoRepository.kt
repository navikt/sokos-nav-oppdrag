package no.nav.sokos.oppdragsinfo.database

import java.sql.Connection
import java.sql.ResultSet
import no.nav.sokos.oppdragsinfo.database.RepositoryExtensions.getColumn
import no.nav.sokos.oppdragsinfo.database.RepositoryExtensions.param
import no.nav.sokos.oppdragsinfo.database.RepositoryExtensions.toList
import no.nav.sokos.oppdragsinfo.database.RepositoryExtensions.withParameters
import no.nav.sokos.oppdragsinfo.domain.Attest
import no.nav.sokos.oppdragsinfo.domain.LinjeStatus
import no.nav.sokos.oppdragsinfo.domain.Oppdrag
import no.nav.sokos.oppdragsinfo.domain.OppdragStatus
import no.nav.sokos.oppdragsinfo.domain.OppdragsDetaljer
import no.nav.sokos.oppdragsinfo.domain.OppdragsEnhet
import no.nav.sokos.oppdragsinfo.domain.OppdragsInfo
import no.nav.sokos.oppdragsinfo.domain.OppdragsLinje


object OppdragsInfoRepository {

    // TODO: hent metadata hvis oppdragsinfo med gjelderid finnes
    fun Connection.getOppdragsInfo(
        gjelderId: String
    ): List<OppdragsInfo> =
        prepareStatement(
            """
                SELECT OPPDRAG_GJELDER_ID
                FROM OS231Q1.T_OPPDRAG
                WHERE OPPDRAG_GJELDER_ID = (?)
            """.trimIndent()
        ).withParameters(
            param(gjelderId)
        ).run {
            executeQuery().toOppdrag()
        }

    fun Connection.getOppdrag(
        gjelderId: String
    ): List<Oppdrag> =
        prepareStatement(
            """
                SELECT OP.OPPDRAGS_ID,
                        OP.FAGSYSTEM_ID,
                        FO.NAVN_FAGOMRAADE,
                        OP.OPPDRAG_GJELDER_ID,
                        OP.TYPE_BILAG,
                        FG.NAVN_FAGGRUPPE,
                        OS.KODE_STATUS,
                        OS.TIDSPKT_REG
                FROM OS231Q1.T_OPPDRAG OP,
                        OS231Q1.T_FAGOMRAADE FO, 
                        OS231Q1.T_FAGGRUPPE FG,
                        OS231Q1.T_OPPDRAG_STATUS OS
                WHERE OP.OPPDRAG_GJELDER_ID = (?)
                AND FO.KODE_FAGOMRAADE = OP.KODE_FAGOMRAADE
                AND FG.KODE_FAGGRUPPE = FO.KODE_FAGGRUPPE
                AND OS.OPPDRAGS_ID = OP.OPPDRAGS_ID
                AND OS.TIDSPKT_REG = (
                SELECT MAX(OS2.TIDSPKT_REG)
                FROM OS231Q1.T_OPPDRAG_STATUS OS2
                WHERE OS2.OPPDRAGS_ID = OS.OPPDRAGS_ID);
            """.trimIndent()
        ).withParameters(
            param(gjelderId)
        ).run {
            executeQuery().toOppdragList()
        }
}

/*fun Connection.hentOppdragslinje(
    oppdragId: Int,
    oppdragslinje: Int
): List<OppdragsLinje> {
    val resultSet = prepareStatement(
        """
                SELECT *
                FROM T_OPPDRAGSLINJE
                WHERE OPPDRAGS_ID = ?
                AND LINJE_ID = ?
            """.trimIndent()
    ).withParameters(
        param(oppdragId),
        param(oppdragslinje)
    ).executeQuery()
    return toOppdragslinje(resultSet)
}*/

/*fun Connection.hentSkyldnere(
    oppdragId: Int,
    oppdragslinje: Int
): List<Skyldner> {
    val resultSet = prepareStatement(
        """
                SELECT *
                FROM T_SKYLDNER
                WHERE OPPDRAGS_ID = ?
                AND LINJE_ID = ?
            """.trimIndent()
    ).withParameters(
        param(oppdragId),
        param(oppdragslinje)
    ).executeQuery()
    return toSkyldner(resultSet)
}*/

/*fun Connection.hentValutaer(
    oppdragId: Int,
    oppdragslinje: Int
): List<Valuta> {
    val resultSet = prepareStatement(
        """
                SELECT *
                FROM T_VALUTA
                WHERE OPPDRAGS_ID = ?
                AND LINJE_ID = ?
            """.trimIndent()
    ).withParameters(
        param(oppdragId),
        param(oppdragslinje)
    ).executeQuery()
    return toValuta(resultSet)
}*/

/*fun Connection.hentLinjeenheter(
    oppdragId: Int,
    oppdragslinje: Int
): List<Linjeenhet> {
    val resultSet = prepareStatement(
        """
                SELECT *
                FROM T_LINJEENHET
                WHERE OPPDRAGS_ID = ?
                AND LINJE_ID = ?
            """.trimIndent()
    ).withParameters(
        param(oppdragId),
        param(oppdragslinje)
    ).executeQuery()
    return toLinjeenhet(resultSet)
}*/

/*fun Connection.hentGrader(
    oppdragId: Int,
    oppdragslinje: Int
): List<Grad> {
    val resultSet = prepareStatement(
        """
                SELECT *
                FROM T_GRAD
                WHERE OPPDRAGS_ID = ?
                AND LINJE_ID = ?
            """.trimIndent()
    ).withParameters(
        param(oppdragId),
        param(oppdragslinje)
    ).executeQuery()
    return toGrad(resultSet)
}*/

/*fun Connection.hentKidlister(
    oppdragId: Int,
    oppdragslinje: Int
): List<Kid> {
    val resultSet = prepareStatement(
        """
                SELECT *
                FROM T_KID
                WHERE OPPDRAGS_ID = ?
                AND LINJE_ID = ?
            """.trimIndent()
    ).withParameters(
        param(oppdragId),
        param(oppdragslinje)
    ).executeQuery()
    return toLKidlist(resultSet)
}*/

/*fun Connection.henOppdragsTekster(
    oppdragId: Int,
    oppdragslinje: Int
): List<OppdragsTekst> {
    val resultSet = prepareStatement(
        """
                SELECT *
                FROM T_TEKST
                WHERE OPPDRAGS_ID = ?
                AND LINJE_ID = ?
            """.trimIndent()
    ).withParameters(
        param(oppdragId),
        param(oppdragslinje)
    ).executeQuery()
    return toOppdragsTekst(resultSet)
}*/


/*fun Connection.henKravhavere(
    oppdragId: Int,
    oppdragslinje: Int
): List<Kravhaver> {
    val resultSet = prepareStatement(
        """
                SELECT *
                FROM T_KRAVHAVER
                WHERE OPPDRAGS_ID = ?
                AND LINJE_ID = ?
            """.trimIndent()
    ).withParameters(
        param(oppdragId),
        param(oppdragslinje)
    ).executeQuery()
    return toKravhaver(resultSet)
}*/

/*fun Connection.henMaksdatoer(
    oppdragId: Int,
    oppdragslinje: Int
): List<Maksdato> {
    val resultSet = prepareStatement(
        """
                SELECT *
                FROM T_MAKS_DATO
                WHERE OPPDRAGS_ID = ?
                AND LINJE_ID = ?
            """.trimIndent()
    ).withParameters(
        param(oppdragId),
        param(oppdragslinje)
    ).executeQuery()
    return toMaksdato(resultSet)
}*/

/*fun Connection.hentKlasse(
    kodeKlasse: String
): List<Klasse> {
    val resultSet = prepareStatement(
        """
                SELECT *
                FROM T_KLASSEKODE
                WHERE KODE_KLASSE = ?
            """.trimIndent()
    ).withParameters(
        param(kodeKlasse)
    ).executeQuery()
    return toKlasse(resultSet)
}*/

fun Connection.hentOppdrag(
    oppdragId: Int
): List<OppdragsDetaljer> =
    prepareStatement(
        """
                SELECT  OP.OPPDRAGS_ID,
                        OP.FAGSYSTEM_ID,
                        OP.KJOR_IDAG,
                        FO.NAVN_FAGOMRAADE,
                        OP.OPPDRAG_GJELDER_ID,
                        OS.KODE_STATUS,
                        OS.TIDSPKT_REG
                FROM    OS231Q1.T_OPPDRAG OP,
                        OS231Q1.T_FAGOMRAADE FO, 
                        OS231Q1.T_OPPDRAG_STATUS OS
                WHERE OP.OPPDRAGS_ID = (?)
                AND FO.KODE_FAGOMRAADE = OP.KODE_FAGOMRAADE
                AND OS.OPPDRAGS_ID = OP.OPPDRAGS_ID
                AND OS.TIDSPKT_REG = (
                SELECT MAX(OS2.TIDSPKT_REG)
                FROM OS231Q1.T_OPPDRAG_STATUS OS2
                WHERE OS2.OPPDRAGS_ID = OS.OPPDRAGS_ID);
            """.trimIndent()
    ).withParameters(
        param(oppdragId)
    ).run {
        executeQuery().toOppdragsDetaljer()
    }


fun Connection.hentOppdragsLinjer(
    oppdragId: Int
): List<OppdragsLinje> =
    prepareStatement(
        """
         SELECT B.OPPDRAGS_ID, B.LINJE_ID, B.ATTESTANT_ID, B.LOPENR, B.DATO_UGYLDIG_FOM, B.BRUKERID, B.TIDSPKT_REG, A.LINJE_ID_KORR, C.KODE_STATUS, C.DATO_FOM, C.TIDSPKT_REG, C.BRUKERID, D.SATS, D.TYPE_SATS, D.KODE_KLASSE, D.DATO_VEDTAK_FOM, D.DATO_VEDTAK_TOM, D.ATTESTERT  
         FROM 
         (SELECT * 
             FROM OS231Q1.T_KORREKSJON 
             WHERE OPPDRAGS_ID = (?) ) A
         FULL JOIN ( SELECT * 
                     FROM OS231Q1.T_ATTESTASJON a1
                     WHERE OPPDRAGS_ID = (?)
                     AND DATO_UGYLDIG_FOM > current_date 
                     AND LOPENR = 
                         ( SELECT MAX(LOPENR) 
                         FROM OS231Q1.T_ATTESTASJON a2 
                         WHERE a2.OPPDRAGS_ID = a1.OPPDRAGS_ID 
                         AND a2.LINJE_ID = a1.LINJE_ID 
                         AND a2.ATTESTANT_ID = a1.ATTESTANT_ID ) 
                     ) B
         ON A.OPPDRAGS_ID = B.OPPDRAGS_ID 
         AND A.LINJE_ID = B.LINJE_ID
         FULL JOIN (SELECT * 
                    FROM OS231Q1.T_LINJE_STATUS LIST
                    WHERE LIST.OPPDRAGS_ID = (?)
                    AND LIST.TIDSPKT_REG = (
                    SELECT MAX(LIS2.TIDSPKT_REG)
                        FROM  OS231Q1.T_LINJE_STATUS LIS2
                        WHERE LIS2.OPPDRAGS_ID = LIST.OPPDRAGS_ID
                        AND LIS2.LINJE_ID = LIST.LINJE_ID
                        AND LIS2.DATO_FOM <= (
                        SELECT MIN(KJPL.DATO_BEREGN_FOM)
                            FROM OS231Q1.T_KJOREPLAN KJPL, OS231Q1.T_OPPDRAG OPPD, OS231Q1.T_FAGOMRAADE FAGO 
                            WHERE KJPL.KODE_FAGGRUPPE 	= FAGO.KODE_FAGGRUPPE
                            AND FAGO.KODE_FAGOMRAADE	     = OPPD.KODE_FAGOMRAADE
                            AND KJPL.STATUS			= 'PLAN'
                            AND KJPL.FREKVENS			= OPPD.FREKVENS
                            AND OPPD.OPPDRAGS_ID		= LIST.OPPDRAGS_ID )
                        ) 
                    ) C
         ON B.OPPDRAGS_ID = C.OPPDRAGS_ID 
         AND B.LINJE_ID = C.LINJE_ID
         FULL JOIN (SELECT * 
            FROM OS231Q1.T_OPPDRAGSLINJE
            WHERE OPPDRAGS_ID = (?) ) D 
        ON C.OPPDRAGS_ID = D.OPPDRAGS_ID 
        AND C.LINJE_ID = D.LINJE_ID
        """.trimIndent()
    ).withParameters(
        param(oppdragId), param(oppdragId), param(oppdragId), param(oppdragId)
    ).run {
        executeQuery().toOppdragsLinjer()
    }


fun Connection.hentOppdragsenheter (
    oppdragsId: Int
): List<OppdragsEnhet> =
    prepareStatement (
        """
            SELECT * 
            FROM T_OPPDRAGSENHET 
            WHERE OPPDRAGS_ID = (?)
            """.trimIndent()
    ).withParameters(
        param(oppdragsId)
    ).run {
        executeQuery().toOppdragsenhet()
    }

private fun ResultSet.toOppdragsenhet() = toList {
    OppdragsEnhet(
        type = getColumn("TYPE_ENHET"),
        enhet = getColumn("ENHET"),
        datoFom = getColumn("DATO_FOM")
    )
}

private fun ResultSet.toOppdrag() = toList {
    OppdragsInfo(
        gjelderId = getColumn("OPPDRAG_GJELDER_ID")
    )
}

private fun ResultSet.toOppdragsDetaljer() = toList {
    OppdragsDetaljer(
        fagsystemId = getColumn("FAGSYSTEM_ID"),
        oppdragsId = getColumn("OPPDRAGS_ID"),
        kjøresIdag = getColumn("KJOR_IDAG"),
        fagOmraadeNavn = getColumn("NAVN_FAGOMRAADE"),
        status = getColumn("KODE_STATUS"),
        oppdragsEnheter = emptyList(),
        oppdragsLinjer = emptyList()
    )
}

private fun ResultSet.toOppdragList() = toList {
    Oppdrag(
        fagsystemId = getColumn("FAGSYSTEM_ID"),
        oppdragsId = getColumn("OPPDRAGS_ID"),
        fagGruppeNavn = getColumn("NAVN_FAGGRUPPE"),
        fagOmraadeNavn = getColumn("NAVN_FAGOMRAADE"),
        bilagsType = getColumn("TYPE_BILAG"),
        status = getColumn("KODE_STATUS")
    )
}

private fun ResultSet.toOppdragsLinjer() = toList {
    OppdragsLinje(
        linjeId = getColumn("LINJE_ID"),
        sats = getColumn("SATS"),
        typeSats = getColumn("TYPE_SATS"),
        vedtakFom = getColumn("DATO_VEDTAK_FOM"),
        vedtakTom = getColumn("DATO_VEDTAK_TOM"),
        kodeKlasse = getColumn("KODE_KLASSE"),
        linjeIdKorreksjon = getColumn("LINJE_ID_KORR"),
        attestert = getColumn("ATTESTERT"),
        status = getColumn("KODE_STATUS")
    )
}

/*fun toOppdragslinje(rs: ResultSet) = rs.toList {
    OppdragsLinje(
        oppdragsId = getColumn("OPPDRAGS_ID"),
        linjeId = getColumn("LINJE_ID"),
        delytelseId = getColumn("DELYTELSE_ID"),
        sats = getColumn("SATS"),
        typeSats = getColumn("TYPE_SATS"),
        vedtakFom = getColumn("DATO_VEDTAK_FOM"),
        vedtakTom = getColumn("DATO_VEDTAK_TOM"),
        kodeKlasse = getColumn("KODE_KLASSE"),
        attestert = getColumn("ATTESTERT"),
        vedtaksId = getColumn("VEDTAK_ID"),
        utbetalesTilId = getColumn("UTBETALES_TIL_ID"),
        refunderesOrgnr = getColumn("REFUNDERES_ID"),
        brukerid = getColumn("BRUKERID"),
        tidspktReg = getColumn("TIDSPKT_REG")
    )
}*/

fun toOppdragstatus(rs: ResultSet) = rs.toList {
    OppdragStatus(
        oppdragsId = getColumn("OPPDRAGS_ID"),
        kode = getColumn("KODE_STATUS"),
        lopenr = getColumn("LOPENR"),
        brukerid = getColumn("BRUKERID"),
        tidspktReg = getColumn("TIDSPKT_REG")
    )
}

fun toAttest(rs: ResultSet) = rs.toList {
    Attest(
        oppdragsId = getColumn("OPPDRAGS_ID"),
        linjeId = getColumn("LINJE_ID"),
        attestantId = getColumn("ATTESTANT_ID"),
        lopenr = getColumn("LOPENR"),
        ugyldigFom = getColumn("DATO_UGYLDIG_FOM"),
        brukerid = getColumn("BRUKERID"),
        tidspktReg = getColumn("TIDSPKT_REG")
    )
}

fun toLinjestatus(rs: ResultSet) = rs.toList {
    LinjeStatus(
        oppdragsId = getColumn("OPPDRAGS_ID"),
        linjeId = getColumn("LINJE_ID"),
        datoFom = getColumn("DATO_FOM"),
        tidspktReg = getColumn("TIDSPKT_REG"),
        brukerid = getColumn("BRUKERID")
    )
}



