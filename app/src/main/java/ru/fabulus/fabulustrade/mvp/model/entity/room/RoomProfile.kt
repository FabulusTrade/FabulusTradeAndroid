package ru.fabulus.fabulustrade.mvp.model.entity.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class RoomProfile(
    @PrimaryKey
    val id: Long = 1,
    val userId: String?,
    val token: String?,
    val deviceToken: String?,
    val hasVisitedTutorial: Boolean
)