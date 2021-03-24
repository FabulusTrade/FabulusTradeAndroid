package ru.wintrade.mvp.model.entity

data class Profile(
    var id: Long? = null,
    var username: String? = null,
    var email: String? = null,
    var avatar: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var patronymic: String? = null,
    val dateJoined: String? = null,
    var token: String? = null,
    var deviceToken: String? = null
)
