package ru.fabulus.fabulustrade.mvp.model.entity.api

import com.google.gson.annotations.Expose

data class ResponseChangePasswordByCode(
    @Expose
    val message: String
)