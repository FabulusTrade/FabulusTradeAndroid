package ru.wintrade.mvp.model.entity.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.wintrade.mvp.model.entity.room.RoomUserProfile

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userProfile: RoomUserProfile)

    @Query("SELECT * FROM RoomUserProfile WHERE id = :id")
    fun get(id: String): RoomUserProfile?

    @Query("DELETE FROM RoomUserProfile WHERE id = :id")
    fun delete(id: Long)
}