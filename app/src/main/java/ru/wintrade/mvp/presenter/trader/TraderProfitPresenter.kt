package ru.wintrade.mvp.presenter.trader

import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import moxy.MvpPresenter
import ru.wintrade.R
import ru.wintrade.mvp.model.data.BarChartData
import ru.wintrade.mvp.model.entity.MonthIndicator
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.entity.TraderStatistic
import ru.wintrade.mvp.model.resource.ResourceProvider
import ru.wintrade.mvp.view.trader.TraderProfitView
import ru.wintrade.util.doubleToStringWithFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class TraderProfitPresenter(
    val traderStatistic: TraderStatistic,
    val trader: Trader
) : MvpPresenter<TraderProfitView>() {

    @Inject
    lateinit var resourceProvider: ResourceProvider

    companion object {
        private const val ZERO_PERCENT = "0.00%"
        private const val PROFITABILITY = "profitability"
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
        trader.pinnedPost?.let { post ->
            viewState.setPinnedPostText(post.text)
        } ?: viewState.setPinnedPostText(null)
        traderStatistic.audienceData[0].followersCount?.let { count ->
            viewState.setFollowersCount(count.toString())
        }
        traderStatistic.amountDeals30?.let { count ->
            viewState.setTradesCount(count.toString())
        }
        initProfitTable()
        getStatisticForYear()
    }

    private fun initProfitTable() {
        clearProfitTable()
        setProfitTable()
    }

    fun getStatisticForYear() {
        viewState.setBtnsState(State.FOR_YEAR)
        initDealsStatisticFields(
            traderStatistic.termOfTransaction365,
            traderStatistic.profitTrades365,
            traderStatistic.losingTrades365,
            traderStatistic.averageProfitTrades365,
            traderStatistic.averageLosingTrades365,
            traderStatistic.incrDecrDepo365,
        )
        initStatisticTable(
            traderStatistic.ratio365Long,
            traderStatistic.ratio365Short,
            traderStatistic.termOfTransaction365Long,
            traderStatistic.termOfTransaction365Short,
            traderStatistic.profitOfPercent365Long,
            traderStatistic.profitOfPercent365Short,
            traderStatistic.percentProfitOfPercent365Long,
            traderStatistic.percentProfitOfPercent365Short,
            traderStatistic.losingOfPercent365Long,
            traderStatistic.losingOfPercent365Short,
            traderStatistic.percentLosingOfPercent365Long,
            traderStatistic.percentLosingOfPercent365Short,
        )
    }

    fun getStatisticLastFifty() {
        viewState.setBtnsState(State.LAST_FIFTY)
        initDealsStatisticFields(
            traderStatistic.termOfTransactionNDeals,
            traderStatistic.profitOfPercentNDeals,
            traderStatistic.losingOfPercentNDeals,
            traderStatistic.averageProfitTradesNDeals,
            traderStatistic.averageLosingTradesNDeals,
            traderStatistic.incrDecrDepoNDeals,
        )
        initStatisticTable(
            traderStatistic.ratioNDealsLong,
            traderStatistic.ratioNDealsShort,
            traderStatistic.termOfTransactionNDealsLong,
            traderStatistic.termOfTransactionNDealsShort,
            traderStatistic.profitOfPercentNDealsLong,
            traderStatistic.profitOfPercentNDealsShort,
            traderStatistic.percentProfitOfPercentNDealsLong,
            traderStatistic.percentProfitOfPercentNDealsShort,
            traderStatistic.losingOfPercentNDealsLong,
            traderStatistic.losingOfPercentNDealsShort,
            traderStatistic.percentLosingOfPercentNDealsLong,
            traderStatistic.percentLosingOfPercentNDealsShort,
        )
    }

    private fun initDealsStatisticFields(
        termOfTransaction: Double?,
        profitOfPercent: Double?,
        losingOfPercent: Double?,
        averageProfitTrades: Double?,
        averageLosingTrades: Double?,
        incrDecrDepo: Double?,
    ) {
        termOfTransaction?.let { term ->
            viewState.setAverageDealsTime(term.toString())
        }
        profitOfPercent?.let { profit ->
            viewState.setPositiveProfitPercentForTransactions("$profit%")
        }
        losingOfPercent?.let { losing ->
            viewState.setNegativeProfitPercentForTransactions("$losing%")
        }
        averageProfitTrades?.let { profit ->
            viewState.setAverageProfitForDeal("$profit%")
        }
        averageLosingTrades?.let { losing ->
            viewState.setAverageLoseForDeal("$losing%")
        }
        incrDecrDepo?.let { value ->
            viewState.setDepoValue("$value%")
        }
    }

    private fun initStatisticTable(
        ratioLong: Double?,
        ratioShort: Double?,
        termOfTransactionLong: Double?,
        termOfTransactionShort: Double?,
        profitOfPercentLong: Double?,
        profitOfPercentShort: Double?,
        percentProfitOfPercentLong: Double?,
        percentProfitOfPercentShort: Double?,
        losingOfPercentLong: Double?,
        losingOfPercentShort: Double?,
        percentLosingOfPercentLong: Double?,
        percentLosingOfPercentShort: Double?,
    ) {
        with(traderStatistic) {
            ratioLong?.let { value ->
                viewState.setAllDealLong("$value%")
            }
            ratioShort?.let { value ->
                viewState.setAllDealShort("$value%")
            }
            termOfTransactionLong?.let { daysCount ->
                viewState.setAvaregeTimeDealLong("$daysCount")
            }
            termOfTransactionShort?.let { daysCount ->
                viewState.setAvaregeTimeDealShort("$daysCount")
            }
            profitOfPercentLong?.let { profit ->
                viewState.setPercentOfProfitDealsLong("$profit%")
            }
            profitOfPercentShort?.let { profit ->
                viewState.setPercentOfProfitDealsShort("$profit%")
            }
            percentProfitOfPercentLong?.let { percent ->
                viewState.setAvaregePercentForProfitDealLong("$percent%")
            }
            percentProfitOfPercentShort?.let { percent ->
                viewState.setAvaregePercentForProfitDealShort("$percent%")
            }
            losingOfPercentLong?.let { percent ->
                viewState.setPercentOfLosingDealsLong("$percent%")
            }
            losingOfPercentShort?.let { percent ->
                viewState.setPercentOfLosingDealsShort("$percent%")
            }
            percentLosingOfPercentLong?.let { percent ->
                viewState.setAvaregePercentForLosingDealLong("$percent%")
            }
            percentLosingOfPercentShort?.let { percent ->
                viewState.setAvaregePercentForLosingDealShort("$percent%")
            }
        }
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
        traderStatistic.monthIndicators.forEach { indicator ->
            when (indicator.month) {
                1 -> viewState.setJanProfit("${indicator.actualProfitMonth}%")
                2 -> viewState.setFebProfit("${indicator.actualProfitMonth}%")
                3 -> viewState.setMarProfit("${indicator.actualProfitMonth}%")
                4 -> viewState.setAprProfit("${indicator.actualProfitMonth}%")
                5 -> viewState.setMayProfit("${indicator.actualProfitMonth}%")
                6 -> viewState.setJunProfit("${indicator.actualProfitMonth}%")
                7 -> viewState.setJulProfit("${indicator.actualProfitMonth}%")
                8 -> viewState.setAugProfit("${indicator.actualProfitMonth}%")
                9 -> viewState.setSepProfit("${indicator.actualProfitMonth}%")
                10 -> viewState.setOctProfit("${indicator.actualProfitMonth}%")
                11 -> viewState.setNovProfit("${indicator.actualProfitMonth}%")
                12 -> viewState.setDecProfit("${indicator.actualProfitMonth}%")
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
            listOf(
                resourceProvider.getColor(R.color.colorGreenBarChart),
                resourceProvider.getColor(R.color.colorBlackBarChart),
                resourceProvider.getColor(R.color.colorRedBarChart)
            )
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