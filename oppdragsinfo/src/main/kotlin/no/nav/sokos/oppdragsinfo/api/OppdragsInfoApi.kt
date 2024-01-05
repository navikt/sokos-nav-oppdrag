package no.nav.sokos.oppdragsinfo.api

import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import no.nav.sokos.oppdragsinfo.api.model.OppdragsInfoRequest
import no.nav.sokos.oppdragsinfo.api.model.OppdragsInfoResponse
import no.nav.sokos.oppdragsinfo.service.OppdragsInfoService

private const val BASE_PATH = "/api/v1/oppdragsinfo"

fun Route.oppdragsInfoApi(
    oppdragsInfoService: OppdragsInfoService = OppdragsInfoService()
) {
    route(BASE_PATH) {

        post("hentOppdrag") {
            val oppdragsInfoRequest: OppdragsInfoRequest = call.receive()
            val response = OppdragsInfoResponse(oppdragsInfoService.hentOppdrag(
                oppdragsInfoRequest.gjelderId,
                call
            ))
            call.respond(response)
        }

        /*        get("oppdrag/{oppdragsId}") {
                    val response = oppdragsInfoService.hentOppdrag(
                        call.parameters["oppdragsId"].orEmpty(),
                        call
                    )
                    call.respond(response)
                }*/

        /*        get("oppdrag/{oppdragsId}/kompakt") {
                    val response = oppdragsInfoService.hentOppdragKompakt(
                        call.parameters.get("oppdragsId").orEmpty(),
                        call
                    )
                    call.respond(response)
                }*/

//        get("oppdrag/{oppdragsId}/detaljer") {
//            val response = OppdragsInfoDetaljerResponse(
//                oppdragsInfoService.hentOppdragsdetaljer (
//                    call.parameters.get("oppdragsId").orEmpty(),
//                    call
//                )
//            )
//            call.respond(response)
//        }

        /*        get("oppdrag/{oppdragsId}/{oppdragslinje}") {
                    val response = OppdragslinjeResponse(
                        oppdragsInfoService.hentOppdragslinje(
                            call.parameters.get("oppdragsId").orEmpty(),
                            call.parameters.get("oppdragslinje").orEmpty(),
                            call
                        )
                    )
                    call.respond(response)
                }*/
    }
}