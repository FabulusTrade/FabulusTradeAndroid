package ru.wintrade.mvp.presenter.traderme

import android.graphics.Color
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.wintrade.R
import ru.wintrade.mvp.model.data.BarChartData
import ru.wintrade.mvp.model.entity.ColorItem
import ru.wintrade.mvp.model.entity.MonthIndicator
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.TraderStatistic
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.model.resource.ResourceProvider
import ru.wintrade.mvp.view.traderme.TraderMeProfitView
import ru.wintrade.navigation.Screens
import ru.wintrade.util.formatString
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class TraderMeProfitPresenter(
    val traderStatistic: TraderStatistic
) : MvpPresenter<TraderMeProfitView>() {

    companion object {
        private const val PROFITABILITY = "profitability"
    }

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var apiRepo: ApiRepo

    enum class State {
        FOR_YEAR, LAST_FIFTY
    }

    @Inject
    lateinit var resourceProvider: ResourceProvider

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
            traderStatistic.colorIncrDecrDepo365,
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
            traderStatistic.colorIncrDecrDepoNDeals,
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
        colorIncrDecrDepo: ColorItem?,
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

        colorIncrDecrDepo?.let { colorItem ->
            var color = resourceProvider.getColor(R.color.colorDarkGray)
            var tmpProfit = resourceProvider.getStringResource(R.string.empty_profit_result)

            colorItem.value?.let {
                tmpProfit = resourceProvider.formatString(R.string.incr_decr_depo_365, it)
            }

            colorItem.color?.let {
                color = Color.parseColor(it)
            }

            viewState.setDepoValue(tmpProfit, color)
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

    private fun clearProfitTable() {
        val colorEmpty = resourceProvider.getColor(R.color.colorDarkGray)
        val profitEmpty = resourceProvider.getStringResource(R.string.empty_profit_result)
        
        viewState.setJanProfit(profitEmpty, colorEmpty)
        viewState.setFebProfit(profitEmpty, colorEmpty)
        viewState.setMarProfit(profitEmpty, colorEmpty)
        viewState.setAprProfit(profitEmpty, colorEmpty)
        viewState.setMayProfit(profitEmpty, colorEmpty)
        viewState.setJunProfit(profitEmpty, colorEmpty)
        viewState.setJulProfit(profitEmpty, colorEmpty)
        viewState.setAugProfit(profitEmpty, colorEmpty)
        viewState.setSepProfit(profitEmpty, colorEmpty)
        viewState.setOctProfit(profitEmpty, colorEmpty)
        viewState.setNovProfit(profitEmpty, colorEmpty)
        viewState.setDecProfit(profitEmpty, colorEmpty)
    }

    private fun setProfitTable() {
        traderStatistic.monthIndicators.forEach { indicator ->
            var color = resourceProvider.getColor(R.color.colorDarkGray)
            var tmpProfit = resourceProvider.getStringResource(R.string.empty_profit_result)

            indicator.colorActualItemMonth?.let { colorActualProfitMonth ->
                colorActualProfitMonth.value?.let {
                    tmpProfit = resourceProvider.formatString(R.string.month_profit, it)
                }

                colorActualProfitMonth.color?.let {
                    color = Color.parseColor(it)
                }
            }
            
            when (indicator.month) {
                1 -> viewState.setJanProfit(tmpProfit, color)
                2 -> viewState.setFebProfit(tmpProfit, color)
                3 -> viewState.setMarProfit(tmpProfit, color)
                4 -> viewState.setAprProfit(tmpProfit, color)
                5 -> viewState.setMayProfit(tmpProfit, color)
                6 -> viewState.setJunProfit(tmpProfit, color)
                7 -> viewState.setJulProfit(tmpProfit, color)
                8 -> viewState.setAugProfit(tmpProfit, color)
                9 -> viewState.setSepProfit(tmpProfit, color)
                10 -> viewState.setOctProfit(tmpProfit, color)
                11 -> viewState.setNovProfit(tmpProfit, color)
                12 -> viewState.setDecProfit(tmpProfit, color)
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

    fun openCreatePostScreen(isPinned: Boolean?, pinnedText: String?) {
        router.navigateTo(Screens.createPostScreen(null, false, isPinned, pinnedText))
    }

    fun onViewResumed() {
        apiRepo
            .readPinnedPost(profile.token!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewState.setPinnedPostText(it.text)
            }, {
                // Ошибка не обрабатывается
            })
    }

    fun deletePinnedText() {
        apiRepo
            .deletePinnedPost(profile.token!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewState.setPinnedPostText(it.text)
            }, {
                // Ошибка не обрабатывается
            })
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