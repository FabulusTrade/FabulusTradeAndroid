package ru.fabulus.fabulustrade.mvp.presenter.base

import android.graphics.Bitmap
import android.util.Log
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.view.base.BaseTraderView
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

abstract class BaseTraderMvpPresenter<View : BaseTraderView> : MvpPresenter<View>() {

    companion object {
        private const val LOG = "VVV"
        private const val AVATAR_NAME = "avatar"
        private const val DATE_TIME_PATTERN = "dd_MM_yyyy_hh_mm_ss"
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
        Log.d(LOG, "BaseTraderMvpPresenter. setImage()")
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
            }.jpeg",
            byteArray.toRequestBody("$AVATAR_NAME/*".toMediaTypeOrNull(), 0, byteArray.size)
        )
        changeAvatar(body)
    }

    private fun changeAvatar(body: MultipartBody.Part) {
        Log.d(LOG, "BaseTraderMvpPresenter. changeAvatar()")
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
                        }, {
                            // Ошибка не обрабатывается
                        })
                }, {
                    // Ошибка не обрабатывается
                })
        }
    }

    fun deleteAvatar() {
        Log.d(LOG, "BaseTraderMvpPresenter. deleteAvatar()")
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

    fun backClicked(): Boolean {
        Log.d(LOG, "BaseTraderMvpPresenter. backClicked()")
        router.exit()
        return true
    }
}