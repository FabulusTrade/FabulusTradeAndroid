package ru.fabulus.fabulustrade.mvp.model.entity

import java.util.*

data class AggregatedTrade(
    val idCompany: Int,
    val company: String,
    val count: Int,
    val companyImg: String,
    val dateLast: Date
)