package com.pkulak.memo

import com.pkulak.memo.storage.BlockStorage
import com.typesafe.config.ConfigFactory
import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module

val memoModule = module {
    val config = ConfigFactory.load()!!

    single {
        Database.connect(config.getString("database.url"),
            driver = config.getString("database.driver"),
            user = config.getString("database.username"),
            password = config.getString("database.password")
        )
    }

    // Storage
    single { BlockStorage(get()) }
}
