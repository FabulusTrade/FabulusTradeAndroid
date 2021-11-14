package ru.wintrade.mvp.presenter.trader

import android.graphics.Color
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import moxy.MvpPresenter
import ru.wintrade.mvp.model.data.BarChartData
import ru.wintrade.mvp.model.entity.MonthIndicator
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.entity.TraderStatistic
import ru.wintrade.mvp.view.trader.TraderProfitView
import ru.wintrade.util.COLOR_GREEN
import ru.wintrade.util.doubleToStringWithFormat
import java.text.SimpleDateFormat
import java.util.*

class TraderProfitPresenter(
    val traderStatistic: TraderStatistic,
    val trader: Trader
) : MvpPresenter<TraderProfitView>() {

    companion object {
        private const val TERM_OF_TRANSACTION_FORMAT = "0"
        private const val DEALS_AVERAGE_PROFIT_FORMAT = "0.0"
        private const val PROFIT_MONTH_FORMAT = "0.00"
        private const val ZERO_PERCENT = "0.00%"
        private const val AVERAGE_PERCENT_DEFAULT = "0.0(0.0%)"
        private const val ZERO = 0
        const val PROFITABILITY = "profitability"
    }

    enum class State {
        FOR_YEAR, LAST_FIFTY
    }

    var isOpen = false
    val entries = BarChartData.getBarChartEntries()
    val labels = BarChartData.getBarChartLabels()
    val checkedYearList = mutableListOf<MonthIndicator>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        viewState.setBtnsState(State.FOR_YEAR)
        viewState.setDateJoined(getTraderDateJoined())
        viewState.setPinnedTextVisible(isOpen)



        trader.pinnedPost?.let { viewState.setPinnedPostText(it.text) }
            ?: viewState.setPinnedPostText(null)
        traderStatistic.audienceData[0].followersCount?.let { viewState.setFollowersCount(it) }
        traderStatistic.amountDeals30?.let { viewState.setTradesCount(it) }
            ?: viewState.setTradesCount(ZERO)
        traderStatistic.termOfTransaction30?.let {
            viewState.setAverageDealsTime(it.doubleToStringWithFormat(TERM_OF_TRANSACTION_FORMAT))
        } ?: viewState.setAverageDealsTime(TERM_OF_TRANSACTION_FORMAT)
        setAverageDealsCountAndProfit()
        clearProfitTable()
        setProfitTable()
    }

    fun getStatisticForYear() {
        viewState.setBtnsState(State.FOR_YEAR)

    }

    fun getStatisticLastFifty() {
       viewState.setBtnsState(State.LAST_FIFTY)

    }

    private fun setAverageDealsCountAndProfit() {
        traderStatistic.profitTrades30?.let {
            viewState.setAverageDealsPositiveCountAndProfit(
                "${it.doubleToStringWithFormat(DEALS_AVERAGE_PROFIT_FORMAT)}(${
                    traderStatistic.averageProfitTrades30?.doubleToStringWithFormat(
                        DEALS_AVERAGE_PROFIT_FORMAT,
                        true
                    )
                })"
            )
        } ?: viewState.setAverageDealsNegativeCountAndProfit(AVERAGE_PERCENT_DEFAULT)
        traderStatistic.losingTrades30?.let {
            viewState.setAverageDealsNegativeCountAndProfit(
                "${it.doubleToStringWithFormat(DEALS_AVERAGE_PROFIT_FORMAT)}(${
                    traderStatistic.averageLosingTrades30?.doubleToStringWithFormat(
                        DEALS_AVERAGE_PROFIT_FORMAT,
                        true
                    )
                })"
            )
        } ?: viewState.setAverageDealsNegativeCountAndProfit(AVERAGE_PERCENT_DEFAULT)
    }

    private fun clearProfitTable() {
        viewState.setJanProfit(ZERO_PERCENT)
        viewState.setFebProfit(ZERO_PERCENT)
        viewState.setMarProfit(ZERO_PERCENT)
        viewState.setAprProfit(ZERO_PERCENT)
        viewState.setMayProfit(ZERO_PERCENT)
        viewState.setJunProfit(ZERO_PERCENT)
        viewState.setJulProfit(ZERO_PERCENT)
        viewState.setAugProfit(ZERO_PERCENT)
        viewState.setSepProfit(ZERO_PERCENT)
        viewState.setOctProfit(ZERO_PERCENT)
        viewState.setNovProfit(ZERO_PERCENT)
        viewState.setDecProfit(ZERO_PERCENT)
    }

    private fun setProfitTable() {
        traderStatistic.monthIndicators.forEach {
            when (it.month) {
                1 -> viewState.setJanProfit(
                    it.actualProfitMonth!!.doubleToStringWithFormat(
                        PROFIT_MONTH_FORMAT, true
                    )
                )
                2 -> viewState.setFebProfit(
                    it.actualProfitMonth!!.doubleToStringWithFormat(
                        PROFIT_MONTH_FORMAT, true
                    )
                )
                3
                -> viewState.setMarProfit(
                    it.actualProfitMonth!!.doubleToStringWithFormat(
                        PROFIT_MONTH_FORMAT, true
                    )
                )
                4
                -> viewState.setAprProfit(
                    it.actualProfitMonth!!.doubleToStringWithFormat(
                        PROFIT_MONTH_FORMAT, true
                    )
                )
                5 -> viewState.setMayProfit(
                    it.actualProfitMonth!!.doubleToStringWithFormat(
                        PROFIT_MONTH_FORMAT, true
                    )
                )
                6 -> viewState.setJunProfit(
                    it.actualProfitMonth!!.doubleToStringWithFormat(
                        PROFIT_MONTH_FORMAT, true
                    )
                )
                7 -> viewState.setJulProfit(
                    it.actualProfitMonth!!.doubleToStringWithFormat(
                        PROFIT_MONTH_FORMAT, true
                    )
                )
                8 -> viewState.setAugProfit(
                    it.actualProfitMonth!!.doubleToStringWithFormat(
                        PROFIT_MONTH_FORMAT, true
                    )
                )
                9 -> viewState.setSepProfit(
                    it.actualProfitMonth!!.doubleToStringWithFormat(
                        PROFIT_MONTH_FORMAT, true
                    )
                )
                10 -> viewState.setOctProfit(
                    it.actualProfitMonth!!.doubleToStringWithFormat(
                        PROFIT_MONTH_FORMAT, true
                    )
                )
                11 -> viewState.setNovProfit(
                    it.actualProfitMonth!!.doubleToStringWithFormat(
                        PROFIT_MONTH_FORMAT, true
                    )
                )
                12 -> viewState.setDecProfit(
                    it.actualProfitMonth!!.doubleToStringWithFormat(
                        PROFIT_MONTH_FORMAT, true
                    )
                )
            }
        }
    }

    private fun getTraderDateJoined(): String {
        val outputDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return outputDateFormat.format(traderStatistic.audienceData[0].dateJoined)
    }

    fun setupBarChart(checkedYear: String): BarData {
        setBarchartData(checkedYear, checkedYearList, entries)
        val barDataSet = BarDataSet(entries, PROFITABILITY)
        barDataSet.colors =
            listOf(COLOR_GREEN, Color.BLACK, Color.RED)
        return BarData(labels, barDataSet)
    }

    private fun setBarchartData(
        checkedYear: String,
        checkedYearList: MutableList<MonthIndicator>,
        entries: MutableList<BarEntry>
    ) {
        traderStatistic.monthIndicators.forEach {
            if (it.year.toString() == checkedYear) {
                checkedYearList.add(it)
            }
        }
        checkedYearList.sortBy { it.month }
        for (i in 0 until checkedYearList.size) {
            if (checkedYearList[i].actualProfitMonth!! >= 0) {
                entries.set(
                    checkedYearList[i].month!!.minus(1),
                    BarEntry(
                        floatArrayOf(
                            checkedYearList[i].actualProfitMonth!!.toFloat(),
                            0f,
                            0f
                        ), checkedYearList[i].month!!.minus(1)
                    )
                )
            } else {
                entries.set(
                    checkedYearList[i].month!!.minus(1),
                    BarEntry(
                        floatArrayOf(
                            0f,
                            0f,
                            checkedYearList[i].actualProfitMonth!!.toFloat(),
                        ), checkedYearList[i].month!!.minus(1)
                    )
                )
            }
        }
    }

    fun setPinnedTextMode() {
        if (isOpen) {
            isOpen = false
            viewState.setPinnedTextVisible(isOpen)
        } else {
            isOpen = true
            viewState.setPinnedTextVisible(isOpen)
        }
    }
}