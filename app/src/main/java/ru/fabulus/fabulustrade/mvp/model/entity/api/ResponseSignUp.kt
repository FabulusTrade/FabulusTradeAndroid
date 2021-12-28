package ru.fabulus.fabulustrade.mvp.model.entity.api

import com.google.gson.annotations.Expose

data class ResponseSignUp(
    @Expose
    val id: String,
    @Expose
    val username: String,
    @Expose
    val password: String,
    @Expose
    val email: String,
    @Expose
    val last_name: String,
    @Expose
    val first_name: String,
    @Expose
    val patronymic: String,
    @Expose
    val phone: String
)