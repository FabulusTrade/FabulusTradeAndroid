package ru.fabulus.fabulustrade.mvp.model.entity.api

import com.google.gson.annotations.Expose

class ResponseUserProfile(
    @Expose
    val id: String,
    @Expose
    val username: String,
    @Expose
    val email: String,
    @Expose
    val avatar: String? = null,
    @Expose
    val kval: Boolean,
    @Expose
    val is_trader: Boolean,
    @Expose
    val first_name: String,
    @Expose
    val last_name: String,
    @Expose
    val patronymic: String,
    @Expose
    val date_joined: String,
    @Expose
    val phone: String? = null,
    @Expose
    val followers_count: Int,
    @Expose
    val subscriptions_count: Int
)