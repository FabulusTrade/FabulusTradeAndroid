package ru.wintrade.mvp.model.entity

data class Profile(
    var id: Long,
    var username: String,
    var email: String,
    var avatar: String? = null,
    var firstName: String,
    var lastName: String,
    var patronymic: String,
    var phone: String? = null,
    var token: String,
    var deviceToken: String
)
