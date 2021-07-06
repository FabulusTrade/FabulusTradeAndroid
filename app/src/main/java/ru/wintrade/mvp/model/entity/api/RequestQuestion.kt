package ru.wintrade.mvp.model.entity.api

import com.google.gson.annotations.Expose

data class RequestQuestion(
    @Expose
    val message_body: String,
    @Expose
    val message_subject: String = "Q"
)