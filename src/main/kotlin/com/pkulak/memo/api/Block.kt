package com.pkulak.memo.api

import com.pkulak.memo.storage.BadRequestException
import com.pkulak.memo.storage.BlockStorage
import com.pkulak.memo.storage.ForbiddenException
import com.pkulak.memo.storage.NotFoundException
import com.pkulak.memo.storage.UnauthorizedException
import com.pkulak.memo.util.accountBytes
import com.pkulak.memo.util.bytes
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.features.origin
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.request.header
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject
import uk.oczadly.karl.jnano.util.CryptoUtil

fun Application.block() {
    val blockStorage: BlockStorage by inject()

    routing {
        post("/blocks") {
            // Authorization: Signature ...
            val sig = call.request.header(HttpHeaders.Authorization)?.substring(10) ?:
                throw ForbiddenException("please include an Authorization header")

            if ((call.request.header(HttpHeaders.ContentLength)?.toInt() ?: Int.MAX_VALUE) > 1_024) {
                throw BadRequestException("body too large, or no Content-Length header")
            }

            val body = call.receive<ByteArray>()
            val req = Json.decodeFromString<BlockRequest>(String(body))

            try {
                if (!CryptoUtil.verifySig(body, sig.bytes(), req.account.accountBytes())) {
                    throw UnauthorizedException("invalid signature")
                }
            } catch (e: IllegalArgumentException) {
                throw UnauthorizedException(e.message ?: "invalid signature")
            }

            if (req.memo.length > 255) {
                throw BadRequestException("memos must be at most 255 characters")
            }

            with (req) {
                if (blockStorage.blockExists(block)) {
                    throw BadRequestException("this block has already been created")
                }

                blockStorage.insertBlock(block, account, link, memo)
            }

            call.respond(HttpStatusCode.OK)
        }

        get("/blocks/{id}/memo") {
            val sig = call.request.header(HttpHeaders.Authorization)?.substring(10) ?:
                throw ForbiddenException("please include an Authorization header")

            val account = call.request.queryParameters["account"] ?:
                throw BadRequestException("please include an account query parameter")

            val block = blockStorage.selectBlock(call.parameters["id"] ?: "") ?:
                throw NotFoundException("block", call.parameters["id"])

            if (!block.account.contentEquals(account.accountBytes())) {
                throw BadRequestException("account submitted doesn't match account in the block")
            }

            val body = call.request.origin.uri.toByteArray()

            try {
                if (!CryptoUtil.verifySig(body, sig.bytes(), block.link)) {
                    throw UnauthorizedException("invalid signature")
                }
            } catch (e: IllegalArgumentException) {
                throw UnauthorizedException(e.message ?: "invalid signature")
            }

            call.respond(HttpStatusCode.OK, block.memo)
        }
    }
}

@Serializable
class BlockRequest(
    val block: String,
    val account: String,
    val link: String,
    val memo: String
)
