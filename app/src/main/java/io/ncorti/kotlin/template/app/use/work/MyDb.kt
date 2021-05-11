package io.ncorti.kotlin.template.app.use.work

import androidx.room.Database
import androidx.room.RoomDatabase
import io.smooth.store.json.JsonModel
import io.smooth.store.json.JsonModelDao

@Database(entities = arrayOf(JsonModel::class), version = 1)
abstract class MyDb: RoomDatabase() {
    abstract fun jsonModelDao(): JsonModelDao
}