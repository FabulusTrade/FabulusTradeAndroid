package ru.wintrade.mvp.model.entity.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class RoomProfile(
    @PrimaryKey
    val id: Long = 1,
    val userId: Long?,
    val token: String?,
    val deviceToken: String?,
    val hasVisitedTutorial: Boolean
)