package ru.wintrade.mvp.model.entity.api

import com.google.gson.annotations.Expose

class ResponseProfile (
    @Expose
    val id: Long,
    @Expose
    val username: String,
    @Expose
    val email: String,
    @Expose
    val avatar: String? = null,
    @Expose
    val firstName: String,
    @Expose
    val lastName: String,
    @Expose
    val patronymic: String,
    @Expose
    val phone: String? = null
)