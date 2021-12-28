package ru.fabulus.fabulustrade.mvp.model.entity.api

import com.google.gson.annotations.Expose

class RequestDevice(
    @Expose
    val registration_id: String,
    @Expose
    val type: String = "android",
    @Expose
    val active: Boolean = true
)