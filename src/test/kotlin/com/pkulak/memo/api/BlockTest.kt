package com.pkulak.memo.api

import com.pkulak.memo.ACCOUNT
import com.pkulak.memo.ACCOUNT2
import com.pkulak.memo.BLOCK_ID
import com.pkulak.memo.PRIVATE_KEY2
import com.pkulak.memo.get
import com.pkulak.memo.init
import com.pkulak.memo.initDb
import com.pkulak.memo.resetDb
import com.pkulak.memo.storage.BlockStorage
import com.pkulak.memo.util.bytes
import io.kotest.matchers.shouldBe
import io.ktor.application.Application
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import uk.oczadly.karl.jnano.util.CryptoUtil

val SIG = "6F0E589A410AE044EBB2ED6942E667B29789BBBCBDCA20B85BD018B92AB5957447372A3EBB9F5C9D4ADA7E8E0E5EF13C739087E25C" +
        "0BA2D2D61298E70991F403"

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BlockTest {
    @BeforeAll
    fun setup() = withTestApplication(Application::init) {
        initDb()
    }

    @BeforeEach
    fun reset() = withTestApplication(Application::init) {
        resetDb()
    }

    @Test
    fun blockCreation() {
        val badSig = "AB6C2AC9EAED69DBA23B0790B97D8DDBB5D005570EF793373FFC5011CD328D8D7E4778BE3DE91603F430AD466C6FE92" +
                "FD7F644CAB912C64EBC2AD0946BFAEE0B"

        createBlock(SIG) shouldBe HttpStatusCode.OK
        createBlock(badSig) shouldBe HttpStatusCode.Unauthorized
    }

    @Test
    fun duplicateBlockCreation() {
        createBlock(SIG) shouldBe HttpStatusCode.OK
        createBlock(SIG) shouldBe HttpStatusCode.BadRequest
    }

    private fun createBlock(signature: String): HttpStatusCode = withTestApplication({ init(); block() }) {
        val bodyObject = BlockRequest(
            block = BLOCK_ID,
            account = ACCOUNT,
            link = ACCOUNT2,
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

    @Test
    fun blockRetrieval() = withTestApplication({ init(); block() }) {
        get<BlockStorage>().insertBlock(BLOCK_ID, ACCOUNT, ACCOUNT2, "A new bike... wow! \uD83D\uDEB4")

        // we sign the full path for authorization
        val path = "/blocks/$BLOCK_ID/memo?account=$ACCOUNT"
        val signature = CryptoUtil.sign(path.toByteArray(), PRIVATE_KEY2.bytes())

        val resp = handleRequest(HttpMethod.Get, path) {
            addHeader(HttpHeaders.Authorization, "Signature $signature")
        }

        resp.response.status() shouldBe HttpStatusCode.OK

        runBlocking {
            resp.response.content shouldBe "A new bike... wow! \uD83D\uDEB4"
        }
    }

    @Test
    fun blockRetrievalBadSig() = withTestApplication({ init(); block() }) {
        get<BlockStorage>().insertBlock(BLOCK_ID, ACCOUNT, ACCOUNT, "A new bike... wow! \uD83D\uDEB4")

        val resp = handleRequest(HttpMethod.Get, "/blocks/$BLOCK_ID/memo?account=$ACCOUNT") {
            addHeader(HttpHeaders.Authorization, "Signature BD79CE85C8AF794FD567C5D5D9F6FBB59353DE1E2AF43E7DB11482")
        }

        resp.response.status() shouldBe HttpStatusCode.Unauthorized
    }
}
