package com.pkulak.memo.storage

import com.pkulak.memo.util.accountBytes
import com.pkulak.memo.util.bytes
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

class BlockStorage(private val db: Database) {
    fun insertBlock(block: String, account: String, link: String, memo: String) = transaction(db) {
        Block.new {
            this.block = block.bytes()
            this.account = account.accountBytes()
            this.link = link.accountBytes()
            this.memo = memo
        }
    }

    fun selectBlock(block: String) = transaction(db) {
        Block
            .find { Blocks.block eq block.bytes() }
            .firstOrNull()
    }

    fun blockExists(block: String) = transaction(db) {
        !Block
            .find { Blocks.block eq block.bytes() }
            .empty()
    }
}
