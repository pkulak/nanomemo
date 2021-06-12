package com.pkulak.memo

import com.pkulak.memo.storage.HttpException
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.StatusPages
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.response.respond
import io.ktor.server.netty.EngineMain
import org.koin.ktor.ext.Koin
import org.koin.logger.SLF4JLogger

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.init() {
    install(CORS) {
        method(HttpMethod.Options)
        header(HttpHeaders.Authorization)
        anyHost()
    }

    install(CallLogging)
    install(Compression)

    install(Koin) {
       SLF4JLogger()
       modules(memoModule)
    }

    install(StatusPages) {
        exception<HttpException> { cause ->
            call.respond(cause.status, cause.message)
        }
    }
}
