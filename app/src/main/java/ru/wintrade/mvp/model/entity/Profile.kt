package ru.wintrade.mvp.model.entity

data class Profile(
    var user: UserProfile?,
    var token: String?,
    var deviceToken: String?,
    var hasVisitedTutorial: Boolean
)