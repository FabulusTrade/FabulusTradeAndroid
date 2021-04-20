package ru.wintrade.mvp.model.entity.common

data class Pagination<T>(
    val count: Int,
    val next: Int?,
    val previous: Int?,
    val results: List<T>
)