package com.pkulak.memo

import com.pkulak.memo.storage.HttpException
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.server.netty.*
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
