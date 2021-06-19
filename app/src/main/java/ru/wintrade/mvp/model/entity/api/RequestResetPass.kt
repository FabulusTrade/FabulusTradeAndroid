package ru.wintrade.mvp.model.entity.api

import com.google.gson.annotations.Expose

data class RequestResetPass(
    @Expose
    val email: String
)