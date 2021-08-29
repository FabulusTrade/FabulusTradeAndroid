package ru.wintrade.mvp.model.entity.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.wintrade.mvp.model.entity.MonthIndicator

data class ResponseMonthIndicators(
    @Expose
    @SerializedName("year")
    override val year: Int,
    @Expose
    @SerializedName("month")
    override val month: Int,
    @Expose
    @SerializedName("actual_profit_month")
    override val actualProfitMonth: Double
) : MonthIndicator
