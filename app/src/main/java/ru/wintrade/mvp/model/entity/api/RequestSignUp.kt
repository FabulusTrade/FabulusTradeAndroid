package ru.wintrade.mvp.model.entity.api

import com.google.gson.annotations.Expose

data class RequestSignUp(
    @Expose
    val username: String,
    @Expose
    val password: String,
    @Expose
    val email: String,
    @Expose
    val phone: String
)