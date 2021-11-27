package ru.wintrade.mvp.presenter.traderme

import android.graphics.Bitmap
import android.graphics.Color
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.wintrade.R
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.model.resource.ResourceProvider
import ru.wintrade.mvp.view.traderme.TraderMeMainView
import ru.wintrade.util.formatDigitWithDef
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class TraderMeMainPresenter : MvpPresenter<TraderMeMainView>() {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var resourceProvider: ResourceProvider

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        loadTraderStatistic()
        profile.user?.let {
            viewState.setSubscriberCount(it.subscriptionsCount)
            viewState.setUsername(it.username)
            it.avatar?.let { viewState.setAvatar(it) }
        }
    }

    private fun loadTraderStatistic() {
        profile.user?.id?.let {
            apiRepo
                .getTraderStatistic(it)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ traderStatistic ->

                    var tmpColor = resourceProvider.getColor(R.color.colorDarkGray)
                    var tmpProfit = resourceProvider.getStringResource(R.string.empty_profit_result)

                    traderStatistic.colorIncrDecrDepo365?.let { colorItem ->
                        tmpProfit = resourceProvider.formatDigitWithDef(
                            R.string.incr_decr_depo_365,
                            colorItem.value
                        )

                        colorItem.color?.let { color ->
                            tmpColor = Color.parseColor(color)
                        }
                    }

                    viewState.setProfit(tmpProfit, tmpColor)

                    viewState.initVP(traderStatistic)
                }, { t ->
                    t.message
                })
        }
    }


    fun backClicked(): Boolean {
        router.exit()
        return true
    }

    fun setImage(image: Bitmap) {
        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val body = MultipartBody.Part.createFormData(
            "avatar",
            "avatar${
                SimpleDateFormat(
                    "dd_MM_yyyy_hh_mm_ss",
                    Locale.getDefault()
                ).format(Date())
            }.jpeg",
            byteArray.toRequestBody("avatar/*".toMediaTypeOrNull(), 0, byteArray.size)
        )
        changeAvatar(body)
    }

    fun changeAvatar(body: MultipartBody.Part) {
        profile.token?.let { token ->
            apiRepo.changeAvatar(token, body).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    apiRepo.getProfile(token).observeOn(AndroidSchedulers.mainThread()).subscribe({
                        viewState.setAvatar(it.avatar)
                    }, {})
                }, {}
            )
        }
    }

    fun deleteAvatar() {
        profile.token?.let { token ->
            apiRepo
                .deleteAvatar(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    apiRepo
                        .getProfile(token)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            viewState.setAvatar(it.avatar)
                        }, {
                            // Ошибка не обрабатывается
                        })
                }, {
                    // Ошибка не обрабатывается
                })
        }
    }
}