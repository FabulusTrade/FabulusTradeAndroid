package ru.wintrade.mvp.model.entity.api

import com.google.gson.annotations.Expose

data class RequestAuth(
    @Expose
    val password: String,
    @Expose
    val username: String
)