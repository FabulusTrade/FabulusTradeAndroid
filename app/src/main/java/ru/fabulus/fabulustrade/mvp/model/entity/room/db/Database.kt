package ru.fabulus.fabulustrade.mvp.model.entity.room.db

import androidx.room.RoomDatabase
import ru.fabulus.fabulustrade.mvp.model.entity.room.RoomProfile
import ru.fabulus.fabulustrade.mvp.model.entity.room.RoomUserProfile
import ru.fabulus.fabulustrade.mvp.model.entity.room.dao.ProfileDao
import ru.fabulus.fabulustrade.mvp.model.entity.room.dao.UserProfileDao

@androidx.room.Database(
    entities = [
        RoomProfile::class,
        RoomUserProfile::class
    ],
    version = 9,
    exportSchema = false
)
abstract class Database : RoomDatabase() {

    abstract fun profileDao(): ProfileDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        const val DB_NAME = "database.db"
    }
}