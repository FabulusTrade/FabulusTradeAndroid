package ru.fabulus.fabulustrade.mvp.presenter.traderme

import android.graphics.Color
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.data.BarChartData
import ru.fabulus.fabulustrade.mvp.model.entity.ColorItem
import ru.fabulus.fabulustrade.mvp.model.entity.MonthIndicator
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.entity.TraderStatistic
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.view.traderme.TraderMeProfitView
import ru.fabulus.fabulustrade.navigation.Screens
import ru.fabulus.fabulustrade.util.formatDigitWithDef
import ru.fabulus.fabulustrade.util.formatString
import java.text.SimpleDateFormat
import java.util.Locale
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
        traderStatistic.averageCountOperationsMonth?.let { count ->
            viewState.setTradesCount(
                resourceProvider.formatString(
                    R.string.tv_deal_for_month_count_text,
                    count
                )
            )
        }
        initProfitTable()
        getStatisticForYear()
    }

    fun initProfitTable() {
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
            ratioLong = traderStatistic.ratio365Long,
            ratioShort = traderStatistic.ratio365Short,
            termOfTransactionLong = traderStatistic.termOfTransaction365Long,
            termOfTransactionShort = traderStatistic.termOfTransaction365Short,
            profitOfPercentLong = traderStatistic.profitOfPercent365Long,
            profitOfPercentShort = traderStatistic.profitOfPercent365Short,
            percentProfitOfPercentLong = traderStatistic.percentProfitOfPercent365Long,
            percentProfitOfPercentShort = traderStatistic.percentProfitOfPercent365Short,
            losingOfPercentLong = traderStatistic.losingOfPercent365Long,
            losingOfPercentShort = traderStatistic.losingOfPercent365Short,
            percentLosingOfPercentLong = traderStatistic.percentLosingOfPercent365Long,
            percentLosingOfPercentShort = traderStatistic.percentLosingOfPercent365Short,
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
            ratioLong = traderStatistic.ratioNDealsLong,
            ratioShort = traderStatistic.ratioNDealsShort,
            termOfTransactionLong = traderStatistic.termOfTransactionNDealsLong,
            termOfTransactionShort = traderStatistic.termOfTransactionNDealsShort,
            profitOfPercentLong = traderStatistic.profitOfPercentNDealsLong,
            profitOfPercentShort = traderStatistic.profitOfPercentNDealsShort,
            percentProfitOfPercentLong = traderStatistic.percentProfitOfPercentNDealsLong,
            percentProfitOfPercentShort = traderStatistic.percentProfitOfPercentNDealsShort,
            losingOfPercentLong = traderStatistic.losingOfPercentNDealsLong,
            losingOfPercentShort = traderStatistic.losingOfPercentNDealsShort,
            percentLosingOfPercentLong = traderStatistic.percentLosingOfPercentNDealsLong,
            percentLosingOfPercentShort = traderStatistic.percentLosingOfPercentNDealsShort,
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

        viewState.setAverageDealsTime(
            resourceProvider.formatDigitWithDef(
                R.string.tv_deal_average_time_value_text,
                termOfTransaction
            )
        )

        viewState.setPositiveProfitPercentForTransactions(
            resourceProvider.formatDigitWithDef(
                R.string.tv_deal_profit_positive_value_text,
                profitOfPercent
            )
        )

        viewState.setNegativeProfitPercentForTransactions(
            resourceProvider.formatDigitWithDef(
                R.string.tv_deal_profit_negative_value_text,
                losingOfPercent
            )
        )
        viewState.setAverageProfitForDeal(
            resourceProvider.formatDigitWithDef(
                R.string.tv_average_profit_value_text,
                averageProfitTrades
            )
        )


        viewState.setAverageLoseForDeal(
            resourceProvider.formatDigitWithDef(
                R.string.tv_average_loss_value_text,
                averageLosingTrades
            )
        )

        colorIncrDecrDepo?.let { colorItem ->
            var tmpColor = resourceProvider.getColor(R.color.colorDarkGray)

            val tmpProfit = resourceProvider.formatDigitWithDef(
                R.string.incr_decr_depo_365,
                colorItem.value
            )

            colorItem.color?.let { color ->
                tmpColor = Color.parseColor(color)
            }

            viewState.setDepoValue(tmpProfit, tmpColor)
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
        viewState.setAllDealLong(
            resourceProvider.formatDigitWithDef(
                R.string.tv_all_deal_long_text,
                ratioLong
            )
        )

        viewState.setAllDealShort(
            resourceProvider.formatDigitWithDef(
                R.string.tv_all_deal_short_text,
                ratioShort
            )
        )

        viewState.setAvaregeTimeDealLong(
            resourceProvider.formatDigitWithDef(
                R.string.tv_average_time_deal_long_text,
                termOfTransactionLong
            )
        )

        viewState.setAvaregeTimeDealShort(
            resourceProvider.formatDigitWithDef(
                R.string.tv_average_time_deal_short_text,
                termOfTransactionShort
            )
        )

        viewState.setPercentOfProfitDealsLong(
            resourceProvider.formatDigitWithDef(
                R.string.tv_prof_deal_long_text,
                profitOfPercentLong
            )
        )

        viewState.setPercentOfProfitDealsShort(
            resourceProvider.formatDigitWithDef(
                R.string.tv_prof_deal_short_text,
                profitOfPercentShort
            )
        )

        viewState.setAvaregePercentForProfitDealLong(
            resourceProvider.formatDigitWithDef(
                R.string.tv_average_percent_profit_deal_long_text,
                percentProfitOfPercentLong
            )
        )

        viewState.setAvaregePercentForProfitDealShort(
            resourceProvider.formatDigitWithDef(
                R.string.tv_average_percent_profit_deal_short_text,
                percentProfitOfPercentShort
            )
        )

        viewState.setPercentOfLosingDealsLong(
            resourceProvider.formatDigitWithDef(
                R.string.tv_less_deal_long_text,
                losingOfPercentLong
            )
        )

        viewState.setPercentOfLosingDealsShort(
            resourceProvider.formatDigitWithDef(
                R.string.tv_less_deal_short_text,
                losingOfPercentShort
            )
        )

        viewState.setAvaregePercentForLosingDealLong(
            resourceProvider.formatDigitWithDef(
                R.string.tv_average_less_deal_long_text,
                percentLosingOfPercentLong
            )
        )

        viewState.setAvaregePercentForLosingDealShort(
            resourceProvider.formatDigitWithDef(
                R.string.tv_average_less_deal_short_text,
                percentLosingOfPercentShort
            )
        )
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
        checkedYearList.forEach { indicator ->
            var tmpColor = resourceProvider.getColor(R.color.colorDarkGray)
            var tmpProfit = resourceProvider.getStringResource(R.string.empty_profit_result)

            indicator.colorActualItemMonth?.let { colorActualProfitMonth ->
                colorActualProfitMonth.value?.let { value ->
                    tmpProfit = resourceProvider.formatString(R.string.month_profit, value)
                }

                colorActualProfitMonth.color?.let { color ->
                    tmpColor = Color.parseColor(color)
                }
            }

            when (indicator.month) {
                1 -> viewState.setJanProfit(tmpProfit, tmpColor)
                2 -> viewState.setFebProfit(tmpProfit, tmpColor)
                3 -> viewState.setMarProfit(tmpProfit, tmpColor)
                4 -> viewState.setAprProfit(tmpProfit, tmpColor)
                5 -> viewState.setMayProfit(tmpProfit, tmpColor)
                6 -> viewState.setJunProfit(tmpProfit, tmpColor)
                7 -> viewState.setJulProfit(tmpProfit, tmpColor)
                8 -> viewState.setAugProfit(tmpProfit, tmpColor)
                9 -> viewState.setSepProfit(tmpProfit, tmpColor)
                10 -> viewState.setOctProfit(tmpProfit, tmpColor)
                11 -> viewState.setNovProfit(tmpProfit, tmpColor)
                12 -> viewState.setDecProfit(tmpProfit, tmpColor)
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
        checkedYearList.clear()
        for (entry in BarChartData.getBarChartEntries()) {
            entries.set(entry.xIndex, entry)
        }
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