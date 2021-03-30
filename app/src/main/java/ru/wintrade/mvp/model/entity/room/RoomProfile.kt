package ru.wintrade.mvp.model.entity.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class RoomProfile(
    val remoteId: Long,
    val username: String,
    val email: String,
    val avatar: String?,
    val firstName: String,
    val lastName: String,
    val patronymic: String,
    val phone: String?,
    val token: String,
    val deviceToken: String,
    val subscriptions_count: Long,
    @PrimaryKey
    val id: Long = 1
)