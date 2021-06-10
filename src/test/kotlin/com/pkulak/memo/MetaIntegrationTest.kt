package com.pkulak.memo

import com.pkulak.memo.storage.Blocks
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

fun init() {
    startKoin {
        modules(memoModule)
    }

    transaction(get()) {
        SchemaUtils.drop(Blocks)
        SchemaUtils.create(Blocks)
    }
}

inline fun <reified T: Any>get(): T = GlobalContext.get().get<T>()