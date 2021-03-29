package ru.wintrade.mvp.model.entity.room.db

import androidx.room.RoomDatabase
import ru.wintrade.mvp.model.entity.room.RoomProfile
import ru.wintrade.mvp.model.entity.room.dao.ProfileDao

@androidx.room.Database(
    entities = [
        RoomProfile::class
    ],
    version = 4,
    exportSchema = false
)
abstract class Database: RoomDatabase() {

    abstract fun profileDao(): ProfileDao

    companion object {
        const val DB_NAME = "database.db"
    }
}