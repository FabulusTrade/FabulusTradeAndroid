package ru.wintrade.mvp.model.entity.api

import com.google.gson.annotations.Expose

data class ResponsePagination<T>(
    @Expose
    val count: Int,
    @Expose
    val next: Int?,
    @Expose
    val previous: Int?,
    @Expose
    val results: List<T>
)