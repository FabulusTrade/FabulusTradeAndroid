package ru.wintrade.mvp.model.entity

data class UserProfile(
    val id: Long,
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