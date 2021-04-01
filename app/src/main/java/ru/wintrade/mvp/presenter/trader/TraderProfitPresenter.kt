package ru.wintrade.mvp.presenter.trader

import android.annotation.SuppressLint
import android.graphics.Color
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.view.trader.TraderProfitView
import java.text.SimpleDateFormat
import javax.inject.Inject

class TraderProfitPresenter(val trader: Trader) : MvpPresenter<TraderProfitView>() {
    @Inject
    lateinit var router: Router

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        viewState.setDateJoined(getTraderDateJoined())
        viewState.setFollowersCount(trader.followersCount)
        viewState.setTradesCount(trader.tradesCount)
    }

    @SuppressLint("SimpleDateFormat")
    fun getTraderDateJoined(): String {
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val outputDateFormat = SimpleDateFormat("dd.MM.yyyy")
        val date = inputDateFormat.parse(trader.dateJoined)
        return outputDateFormat.format(date!!)
    }

    fun setupBarChart(): BarData {
        val entries = listOf(
            BarEntry(floatArrayOf(0f, 0f, 0f), 0),
            BarEntry(floatArrayOf(0f, 0f, 0f), 1),
            BarEntry(floatArrayOf(0f, 0f, 0f), 2),
            BarEntry(floatArrayOf(0f, 0f, -1.4f), 3),
            BarEntry(floatArrayOf(3.2f, 0f, 0f), 4),
            BarEntry(floatArrayOf(5.1f, 0f, 0f), 5),
            BarEntry(floatArrayOf(2.9f, 0f, 0f), 6)
        )
        val labels = listOf(
            "Янв", "Фев", "Мар", "Апр", "Май", "Июн", "Июл", "Авг", "Сен", "Окт", "Ноя", "Дек"
        )
        val barDataSet = BarDataSet(entries, "profitability")
        barDataSet.colors =
            listOf(Color.GREEN, Color.BLACK, Color.RED)
        return BarData(labels, barDataSet)
    }
}