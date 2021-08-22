package ru.wintrade.mvp.presenter.traderme

import android.graphics.Bitmap
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.view.traderme.TraderMeMainView
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

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        viewState.setProfit("146%", true)
        viewState.setSubscriberCount(profile.user!!.subscriptionsCount)
        viewState.setUsername(profile.user!!.username)
        profile.user!!.avatar?.let { viewState.setAvatar(it) }
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
            "avatar${SimpleDateFormat("dd_MM_yyyy_hh_mm_ss").format(Date())}.jpeg",
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
            apiRepo.deleteAvatar(token).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    apiRepo.getProfile(token).observeOn(AndroidSchedulers.mainThread()).subscribe({
                        viewState.setAvatar(it.avatar)
                    }, {})
                }, {})
        }
    }
}