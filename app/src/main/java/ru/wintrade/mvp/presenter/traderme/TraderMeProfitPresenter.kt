package ru.wintrade.mvp.presenter.traderme

import android.annotation.SuppressLint
import android.graphics.Color
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.view.traderme.TraderMeProfitView
import ru.wintrade.navigation.Screens
import java.text.SimpleDateFormat
import javax.inject.Inject

class TraderMeProfitPresenter() :
    MvpPresenter<TraderMeProfitView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var apiRepo: ApiRepo

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        viewState.setDateJoined(getTraderDateJoined())
        viewState.setFollowersCount(profile.user!!.followersCount)
        viewState.setTradesCount(10)
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
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val outputDateFormat = SimpleDateFormat("dd.MM.yyyy")
        val date = inputDateFormat.parse(profile.user!!.dateJoined)
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

    fun openCreatePostScreen(isPinned: Boolean?, pinnedText: String?) {
        router.navigateTo(Screens.CreatePostScreen(isPinned, pinnedText))
    }
}