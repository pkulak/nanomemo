package com.pkulak.memo.storage

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Blocks : IntIdTable("blocks") {
    val block = binary("block").index(isUnique = true)
    val account = binary("account")
    val link = binary("link")
    val memo = varchar("memo", 255)
}

class Block(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Block>(Blocks)

    var block by Blocks.block
    var account by Blocks.account
    var link by Blocks.link
    var memo by Blocks.memo
}