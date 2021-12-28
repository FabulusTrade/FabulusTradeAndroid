package ru.fabulus.fabulustrade.mvp.model.entity.api

import com.google.gson.annotations.Expose

class ResponseLikes(
    @Expose
    val like_value: Int,
    @Expose
    val author_id: String,
    @Expose
    val like_count: Int,
    @Expose
    val dislike_count: Int
)