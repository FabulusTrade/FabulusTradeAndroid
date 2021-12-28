package ru.fabulus.fabulustrade.mvp.model.entity.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomUserProfile(
    @PrimaryKey
    var id: String,
    var username: String,
    var email: String,
    var avatar: String? = null,
    var kval: Boolean,
    var isTrader: Boolean,
    var firstName: String,
    var lastName: String,
    var patronymic: String,
    var dateJoined: String,
    var phone: String? = null,
    var followersCount: Int,
    var subscriptionsCount: Int
)