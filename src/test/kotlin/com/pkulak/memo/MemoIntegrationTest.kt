package com.pkulak.memo

import com.pkulak.memo.storage.Blocks
import io.ktor.server.engine.ApplicationEngine
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ktor.ext.get
import org.koin.ktor.ext.inject

const val BLOCK_ID = "5855E3B99E1E1DE391B9E24A2E05010B604E1F26F4ABEC194F7A589B3C62F80C"

const val ACCOUNT = "nano_1pu7p5n3ghq1i1p4rhmek41f5add1uh34xpb94nkbxe8g4a6x1p69emk8y1d"
const val PRIVATE_KEY = "3BE4FC2EF3F3B7374E6FC4FB6E7BB153F8A2998B3B3DAB50853EABE128024143"

const val ACCOUNT2 = "nano_1jwc9njxqe4dhth8au79ifxij1x5k5hwmn6eoprat47yfoataxqjbgsjrery"
const val PRIVATE_KEY2 = "7C61AD72D80ABCD96950D8795CEBCE9AB7FF10BAA8550D9086EC60D53DE9CA1D"

fun ApplicationEngine.initDb() {
     transaction(application.get()) {
        SchemaUtils.drop(Blocks)
        SchemaUtils.create(Blocks)
    }
}

fun ApplicationEngine.resetDb() {
    transaction(application.get()) {
        Blocks.deleteAll()
    }
}

inline fun <reified T: Any> ApplicationEngine.inject() = application.inject<T>()

inline fun <reified T: Any> ApplicationEngine.get() = application.get<T>()
