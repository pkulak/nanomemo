package com.pkulak.memo.storage

import com.pkulak.memo.get
import com.pkulak.memo.init
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class BlockStorageTest {
    @Test
    fun insertAndSelect() {
        init()

        val blockId = "5855E3B99E1E1DE391B9E24A2E05010B604E1F26F4ABEC194F7A589B3C62F80C"

        get<BlockStorage>().insertBlock(
            blockId,
            "nano_3pj51ife78z89jbidu7k3trfshf7bi6ntxeuawbr63nbmrpj54ospiaxx8uc",
            "nano_3swpttz8t86zywz7xa83wb9ygsq89y71i7eyg9ackeix1nubzng9uj7aw9ha",
            "Pizza! \uD83C\uDF55"
        )

        val block = get<BlockStorage>().selectBlock(blockId)!!

        block.memo shouldBe "Pizza! \uD83C\uDF55"
    }
}