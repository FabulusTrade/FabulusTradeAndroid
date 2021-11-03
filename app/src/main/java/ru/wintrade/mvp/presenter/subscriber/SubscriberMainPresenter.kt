package ru.wintrade.mvp.presenter.subscriber

import android.graphics.Bitmap
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.view.subscriber.SubscriberMainView
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class SubscriberMainPresenter : MvpPresenter<SubscriberMainView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profile: Profile

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        viewState.setAvatar(profile.user!!.avatar)
        viewState.setName(profile.user!!.username)
        getSubscriptionCount()
    }

    fun getSubscriptionCount() {
        apiRepo
            .getProfile(profile.token!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewState.setSubscriptionCount(it.subscriptions_count)
            }, {
                it.printStackTrace()
            })
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