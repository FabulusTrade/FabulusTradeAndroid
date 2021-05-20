package ru.wintrade.mvp.model.entity.room.db

import androidx.room.RoomDatabase
import ru.wintrade.mvp.model.entity.room.RoomProfile
import ru.wintrade.mvp.model.entity.room.RoomUserProfile
import ru.wintrade.mvp.model.entity.room.dao.ProfileDao
import ru.wintrade.mvp.model.entity.room.dao.UserProfileDao

@androidx.room.Database(
    entities = [
        RoomProfile::class,
        RoomUserProfile::class
    ],
    version = 8,
    exportSchema = false
)
abstract class Database : RoomDatabase() {

    abstract fun profileDao(): ProfileDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        const val DB_NAME = "database.db"
    }
}