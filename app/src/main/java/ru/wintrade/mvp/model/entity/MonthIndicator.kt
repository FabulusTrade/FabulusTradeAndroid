package ru.wintrade.mvp.model.entity

import java.io.Serializable

interface MonthIndicator : Serializable {
    val year: Int
    val month: Int
    val actualProfitMonth: Double
}