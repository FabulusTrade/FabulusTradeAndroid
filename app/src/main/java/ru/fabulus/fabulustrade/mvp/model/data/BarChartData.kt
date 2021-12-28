package ru.fabulus.fabulustrade.mvp.model.data

import com.github.mikephil.charting.data.BarEntry

object BarChartData {
    fun getBarChartEntries() =
        mutableListOf(
            BarEntry(floatArrayOf(0f, 0f, 0f), 0),
            BarEntry(floatArrayOf(0f, 0f, 0f), 1),
            BarEntry(floatArrayOf(0f, 0f, 0f), 2),
            BarEntry(floatArrayOf(0f, 0f, 0f), 3),
            BarEntry(floatArrayOf(0f, 0f, 0f), 4),
            BarEntry(floatArrayOf(0f, 0f, 0f), 5),
            BarEntry(floatArrayOf(0f, 0f, 0f), 6),
            BarEntry(floatArrayOf(0f, 0f, 0f), 7),
            BarEntry(floatArrayOf(0f, 0f, 0f), 8),
            BarEntry(floatArrayOf(0f, 0f, 0f), 9),
            BarEntry(floatArrayOf(0f, 0f, 0f), 10),
            BarEntry(floatArrayOf(0f, 0f, 0f), 11)
        )

    fun getBarChartLabels() = listOf(
        "Янв",
        "Фев",
        "Мар",
        "Апр",
        "Май",
        "Июн",
        "Июл",
        "Авг",
        "Сен",
        "Окт",
        "Ноя",
        "Дек"
    )
}