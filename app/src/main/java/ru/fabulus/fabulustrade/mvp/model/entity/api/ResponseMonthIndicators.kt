package ru.fabulus.fabulustrade.mvp.model.entity.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.fabulus.fabulustrade.mvp.model.entity.ColorItem

data class ResponseMonthIndicators(
    @Expose
    @SerializedName("year")
    val year: Int?,
    @Expose
    @SerializedName("month")
    val month: Int?,
    @Expose
    @SerializedName("actual_profit_month")
    val actualProfitMonth: Double?,
    @Expose
    @SerializedName("color_actual_profit_month")
    val colorActualItemMonth: ColorItem?
)