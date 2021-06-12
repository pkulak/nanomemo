package com.pkulak.memo.storage

import com.pkulak.memo.ACCOUNT
import com.pkulak.memo.BLOCK_ID
import com.pkulak.memo.init
import com.pkulak.memo.initDb
import com.pkulak.memo.inject
import io.kotest.matchers.shouldBe
import io.ktor.application.Application
import io.ktor.server.testing.withTestApplication
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BlockStorageTest {
    @BeforeAll
    fun setup() = withTestApplication(Application::init) {
        initDb()
    }

    @Test
    fun insertAndSelect() = withTestApplication(Application::init) {
        val blockStorage: BlockStorage by inject()
        blockStorage.insertBlock(BLOCK_ID, ACCOUNT, ACCOUNT, "Pizza! \uD83C\uDF55")
        blockStorage.selectBlock(BLOCK_ID)?.memo shouldBe "Pizza! \uD83C\uDF55"
    }
}
