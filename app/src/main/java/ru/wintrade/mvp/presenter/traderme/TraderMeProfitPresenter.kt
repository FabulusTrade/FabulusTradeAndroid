package ru.wintrade.mvp.presenter.traderme

import android.annotation.SuppressLint
import android.graphics.Color
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.wintrade.mvp.model.data.BarChartData
import ru.wintrade.mvp.model.entity.MonthIndicator
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.TraderStatistic
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.presenter.trader.TraderProfitPresenter
import ru.wintrade.mvp.view.traderme.TraderMeProfitView
import ru.wintrade.navigation.Screens
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import javax.inject.Inject

class TraderMeProfitPresenter(val traderStatistic: TraderStatistic) :
    MvpPresenter<TraderMeProfitView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var apiRepo: ApiRepo

    var isOpen = false
    val entries = BarChartData.getBarChartEntries()
    val labels = BarChartData.getBarChartLabels()
    val checkedYearList = mutableListOf<MonthIndicator>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        viewState.setDateJoined(getTraderDateJoined())
        traderStatistic.audienceData[0].followersCount?.let { viewState.setFollowersCount(it) }
        traderStatistic.amountDeals30?.let { viewState.setTradesCount(it) }
            ?: viewState.setTradesCount(0)
        viewState.setPinnedTextVisible(isOpen)
        traderStatistic.termOfTransaction30?.let {
            viewState.setAverageDealsTime(
                DecimalFormat("0.0").format(
                    traderStatistic.termOfTransaction30
                )
            )
        } ?: viewState.setAverageDealsTime("0.0")
        setAverageDealsCountAndProfit()
        clearProfitTable()
        setProfitTable()
    }

    private fun setAverageDealsCountAndProfit() {
        traderStatistic.profitTrades30?.let {
            viewState.setAverageDealsPositiveCountAndProfit(
                "${DecimalFormat("0.0").format(traderStatistic.profitTrades30)}(${
                    DecimalFormat("0.0").format(
                        traderStatistic.averageProfitTrades30
                    )
                }%)"
            )
        } ?: viewState.setAverageDealsNegativeCountAndProfit("0.0 ( 0.0%)")
        traderStatistic.losingTrades30?.let {
            viewState.setAverageDealsNegativeCountAndProfit(
                "${DecimalFormat("0.0").format(traderStatistic.losingTrades30)}(${
                    DecimalFormat("0.0").format(
                        traderStatistic.averageLosingTrades30
                    )
                }%)"
            )
        } ?: viewState.setAverageDealsNegativeCountAndProfit("0.0 ( 0.0%)")
    }

    private fun clearProfitTable() {
        viewState.setJanProfit("0.00%")
        viewState.setFebProfit("0.00%")
        viewState.setMarProfit("0.00%")
        viewState.setAprProfit("0.00%")
        viewState.setMayProfit("0.00%")
        viewState.setJunProfit("0.00%")
        viewState.setJulProfit("0.00%")
        viewState.setAugProfit("0.00%")
        viewState.setSepProfit("0.00%")
        viewState.setOctProfit("0.00%")
        viewState.setNovProfit("0.00%")
        viewState.setDecProfit("0.00%")
    }

    private fun setProfitTable() {
        traderStatistic.monthIndicators.forEach {
            when (it.month) {
                1 -> viewState.setJanProfit("${DecimalFormat("0.00").format(it.actualProfitMonth)}%")
                2 -> viewState.setFebProfit("${DecimalFormat("0.00").format(it.actualProfitMonth)}%")
                3 -> viewState.setMarProfit("${DecimalFormat("0.00").format(it.actualProfitMonth)}%")
                4 -> viewState.setAprProfit("${DecimalFormat("0.00").format(it.actualProfitMonth)}%")
                5 -> viewState.setMayProfit("${DecimalFormat("0.00").format(it.actualProfitMonth)}%")
                6 -> viewState.setJunProfit("${DecimalFormat("0.00").format(it.actualProfitMonth)}%")
                7 -> viewState.setJulProfit("${DecimalFormat("0.00").format(it.actualProfitMonth)}%")
                8 -> viewState.setAugProfit("${DecimalFormat("0.00").format(it.actualProfitMonth)}%")
                9 -> viewState.setSepProfit("${DecimalFormat("0.00").format(it.actualProfitMonth)}%")
                10 -> viewState.setOctProfit("${DecimalFormat("0.00").format(it.actualProfitMonth)}%")
                11 -> viewState.setNovProfit("${DecimalFormat("0.00").format(it.actualProfitMonth)}%")
                12 -> viewState.setDecProfit("${DecimalFormat("0.00").format(it.actualProfitMonth)}%")
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

    fun onViewResumed() {
        apiRepo.readPinnedPost(profile.token!!).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                viewState.setPinnedPostText(it.text)
            },
            {}
        )
    }

    fun deletePinnedText() {
        apiRepo.deletePinnedPost(profile.token!!)
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                viewState.setPinnedPostText(it.text)
            },
                {}
            )
    }

    @SuppressLint("SimpleDateFormat")
    fun getTraderDateJoined(): String {
        val outputDateFormat = SimpleDateFormat("dd.MM.yyyy")
        return outputDateFormat.format(traderStatistic.audienceData[0].dateJoined)
    }

    fun setupBarChart(checkedYear: String): BarData {
        setBarchartData(checkedYear, checkedYearList, entries)
        val barDataSet = BarDataSet(entries, TraderProfitPresenter.PROFITABILITY)
        barDataSet.colors =
            listOf(Color.GREEN, Color.BLACK, Color.RED)
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
        router.navigateTo(Screens.CreatePostScreen(null, false, isPinned, pinnedText))
    }

    fun showDialog() {
        viewState.showInfoDialog()
    }
}