package ru.wintrade.mvp.model.entity.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.wintrade.mvp.model.entity.room.RoomProfile

@Dao
interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(profile: RoomProfile)

    @Query("SELECT * FROM RoomProfile WHERE id = 1")
    fun get(): RoomProfile?

    @Query("DELETE FROM RoomProfile WHERE id = 1")
    fun delete()
}