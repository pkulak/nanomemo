package com.pkulak.memo.storage

import io.ktor.http.*

open class HttpException : Exception() {
    open val status = HttpStatusCode.InternalServerError
    override val message = "Something has gone terribly wrong!"
}

class ForbiddenException(override val message: String) : HttpException() {
    override val status = HttpStatusCode.Forbidden
}

class UnauthorizedException(override val message: String) : HttpException() {
    override val status = HttpStatusCode.Unauthorized
}

class BadRequestException(override val message: String) : HttpException() {
    override val status = HttpStatusCode.BadRequest
}

class NotFoundException(val type: String, val id: String?) : HttpException() {
    override val status = HttpStatusCode.NotFound

    override val message = if (id == null) {
            "$type not found"
        } else {
            "$type with id \"$id\" not found"
        }
}