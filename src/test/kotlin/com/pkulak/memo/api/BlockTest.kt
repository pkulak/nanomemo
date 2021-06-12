package com.pkulak.memo.api

import com.pkulak.memo.ACCOUNT
import com.pkulak.memo.BLOCK_ID
import com.pkulak.memo.init
import com.pkulak.memo.initDb
import io.kotest.matchers.shouldBe
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BlockTest {
    @BeforeAll
    fun setup() = withTestApplication(Application::init) {
        initDb()
    }

    @Test
    fun blockCreation() {
        val goodSig = "DB6C2AC9EAED69DBA23B0790B97D8DDBB5D005570EF793373FFC5011CD328D8D7E4778BE3DE91603F430AD466C6FE9" +
                "2FD7F644CAB912C64EBC2AD0946BFAEE0B"
        val badSig = "AB6C2AC9EAED69DBA23B0790B97D8DDBB5D005570EF793373FFC5011CD328D8D7E4778BE3DE91603F430AD466C6FE92" +
                "FD7F644CAB912C64EBC2AD0946BFAEE0B"

        createBlock(goodSig) shouldBe HttpStatusCode.OK
        createBlock(badSig) shouldBe HttpStatusCode.Unauthorized
    }

    private fun createBlock(signature: String): HttpStatusCode = withTestApplication({ init(); block() }) {
        val bodyObject = BlockRequest(
            block = BLOCK_ID,
            account = ACCOUNT,
            link = ACCOUNT,
            memo = "Pizza! \uD83C\uDF55"
        )

        val bodyBytes = Json.encodeToString(bodyObject).toByteArray()

        val resp = handleRequest(HttpMethod.Post, "/blocks") {
            addHeader(HttpHeaders.ContentType, "application/json")
            addHeader(HttpHeaders.ContentLength, bodyBytes.size.toString())
            addHeader(HttpHeaders.Authorization, "Signature $signature")
            setBody(bodyBytes)
        }

        resp.response.status()!!
    }
}