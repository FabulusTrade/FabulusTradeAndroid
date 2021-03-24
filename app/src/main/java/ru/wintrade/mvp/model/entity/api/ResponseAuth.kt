package ru.wintrade.mvp.model.entity.api

import com.google.gson.annotations.Expose

data class ResponseAuth(
    @Expose
    val auth_token: String
)