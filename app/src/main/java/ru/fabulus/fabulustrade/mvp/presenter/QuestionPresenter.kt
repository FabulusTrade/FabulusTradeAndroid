package ru.fabulus.fabulustrade.mvp.presenter

import android.graphics.Bitmap
import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import ru.fabulus.fabulustrade.mvp.view.QuestionView
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlin.math.min

class QuestionPresenter : MvpPresenter<QuestionView>() {
    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var apiRepo: ApiRepo

    private val images = mutableListOf<ByteArray>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        profile.user?.email?.let { viewState.setEmail(it) }
    }

    fun sendMessage(
        msg: String,
        email: String,
        theme: String,
    ) {
        if (profile.token != null) {
            apiRepo
                .sendQuestionAuthorizedUser(
                    token = profile.token.toString(),
                    message_body = msg,
                    email = email,
                    theme = theme,
                    images = images
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.showToast()
                    viewState.clearField()
                }, {
                    Log.d("TEST_TAG", "sendMessage throwable", it)
                })
        }else{
            apiRepo
                .sendQuestionNotAuthorizedUser(
                    message_body = msg,
                    email = email,
                    theme = theme,
                    images = images
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.showToast()
                    viewState.clearField()
                }, {
                    Log.d("TEST_TAG", "sendMessage throwable", it)
                })
        }
    }

    fun deleteImage(image: ByteArray) {
        images.remove(image)
        viewState.updateListOfImages(images.toList())
    }

    fun deleteAllImage() {
        images.clear()
        viewState.updateListOfImages(images.toList())
    }

    fun addImages(newImages: List<Bitmap>) {
        if (newImages.isEmpty()) return
        val remain = MAX_ATTACHED_IMAGE_COUNT
        val imageCountToAdd = min(newImages.size, remain)
        repeat(imageCountToAdd) { addImage(newImages[it]) }
        viewState.showImagesAddedMessage(imageCountToAdd)
        viewState.updateListOfImages(images.toList())
    }

    private fun addImage(imageBitmap: Bitmap) {
        val stream = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        images.add(stream.toByteArray())
    }

    companion object {
        private const val MAX_ATTACHED_IMAGE_COUNT = 4
    }
}