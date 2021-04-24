package ru.wintrade.mvp.model.entity.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class RoomProfile(
    val remoteId: Long,
    val username: String,
    val email: String,
    val avatar: String?,
    val kval: Boolean,
    val isTrader: Boolean,
    val firstName: String,
    val lastName: String,
    val patronymic: String,
    val dateJoined: String,
    val phone: String?,
    val followersCount: Int,
    val subscriptionsCount: Int,
    val token: String,
    val deviceToken: String,
    @PrimaryKey
    val id: Long = 1
)