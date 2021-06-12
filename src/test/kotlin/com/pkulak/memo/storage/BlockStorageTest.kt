package com.pkulak.memo.storage

import com.pkulak.memo.*
import io.kotest.matchers.shouldBe
import io.ktor.application.*
import io.ktor.server.testing.*
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