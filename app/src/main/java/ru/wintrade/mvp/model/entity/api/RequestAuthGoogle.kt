package ru.wintrade.mvp.model.entity.api

import com.google.gson.annotations.Expose

data class RequestAuthGoogle(
    @Expose
    val id_token: String
)