package ru.fabulus.fabulustrade.mvp.presenter.base

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.entity.TraderStatistic
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.view.base.BaseTraderView
import ru.fabulus.fabulustrade.util.formatDigitWithDef
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

abstract class BaseTraderMvpPresenter<View : BaseTraderView> : MvpPresenter<View>() {

    companion object {
        private const val AVATAR_NAME = "avatar"
        private const val DATE_TIME_PATTERN = "dd_MM_yyyy_hh_mm_ss"
        private const val AVATAR_FILE_EXTENSION = "jpeg"
    }

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var resourceProvider: ResourceProvider

    fun setImage(image: Bitmap) {
        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val body = MultipartBody.Part.createFormData(
            AVATAR_NAME,
            "$AVATAR_NAME${
                SimpleDateFormat(
                    DATE_TIME_PATTERN,
                    Locale.getDefault()
                ).format(Date())
            }.$AVATAR_FILE_EXTENSION",
            byteArray.toRequestBody("$AVATAR_NAME/*".toMediaTypeOrNull(), 0, byteArray.size)
        )
        changeAvatar(body)
    }

    private fun changeAvatar(body: MultipartBody.Part) {
        profile.token?.let { token ->
            apiRepo
                .changeAvatar(token, body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    apiRepo
                        .getProfile(token)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            viewState.setAvatar(it.avatar)
                        }, { throwable ->
                            Log.e(BaseTraderMvpPresenter::class.java.name, throwable.message ?: "")
                        })
                }, { throwable ->
                    Log.e(BaseTraderMvpPresenter::class.java.name, throwable.message ?: "")
                })
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
                        }, { throwable ->
                            Log.e(BaseTraderMvpPresenter::class.java.name, throwable.message ?: "")
                        })
                }, { throwable ->
                    Log.e(BaseTraderMvpPresenter::class.java.name, throwable.message ?: "")
                })
        }
    }

    protected fun getProfitAndColor(traderStatistic: TraderStatistic): Pair<Int, String> {
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
        return Pair(tmpColor, tmpProfit)
    }

    fun backClicked(): Boolean {
        router.exit()
        return true
    }
}