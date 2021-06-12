package com.pkulak.memo

import com.pkulak.memo.storage.BlockStorage
import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module

val memoModule = module {
    // TODO: some configuration system
    single {
        Database.connect("jdbc:postgresql://localhost/memo",
            driver = "org.postgresql.Driver",
            user = "memo",
            password = "memo"
        )
    }

    // Storage
    single { BlockStorage(get()) }
}