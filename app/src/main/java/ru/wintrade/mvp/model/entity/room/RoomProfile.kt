package ru.wintrade.mvp.model.entity.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class RoomProfile(
    val remoteId: Long?,
    val username: String?,
    val email: String?,
    val avatar: String?,
    val firstName: String?,
    val lastName: String?,
    val patronymic: String?,
    val dateJoined: String?,
    val token: String?,
    val deviceToken: String?,
    @PrimaryKey
    val id: Long = 1
)