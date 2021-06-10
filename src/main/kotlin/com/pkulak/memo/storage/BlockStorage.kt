package com.pkulak.memo.storage

import com.pkulak.memo.util.bytes
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import uk.oczadly.karl.jnano.model.NanoAccount

class BlockStorage(private val db: Database) {
    fun insertBlock(block: String, account: String, link: String, memo: String) = transaction(db) {
        Block.new {
            this.block = block.bytes()
            this.account = NanoAccount.parseAddress(account).toPublicKey().bytes()
            this.link = NanoAccount.parseAddress(link).toPublicKey().bytes()
            this.memo = memo
        }
    }

    fun selectBlock(block: String) = transaction(db) {
        Block
            .find { Blocks.block eq block.bytes() }
            .firstOrNull()
    }
}