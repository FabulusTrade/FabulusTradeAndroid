package ru.fabulus.fabulustrade.mvp.model.entity

import java.util.Date

data class Deals(
    val date: Date,
    val logo: String,
    val operation: String,
    val profit: String?
)