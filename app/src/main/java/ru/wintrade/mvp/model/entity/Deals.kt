package ru.wintrade.mvp.model.entity

import java.util.*

data class Deals(
    val date: Date,
    val logo: String,
    val operation: String,
    val profit: String?
)