package ru.wintrade.mvp.presenter

import android.graphics.Bitmap
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.view.CreatePostView
import ru.wintrade.util.ImageHelper
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

class CreatePostPresenter(
    private val isPublication: Boolean,
    private val isPinnedEdit: Boolean?
) : MvpPresenter<CreatePostView>() {

    companion object {
        const val NEW_POST_RESULT = "NEW_POST_RESULT"
        private const val MAX_ATTACHED_IMAGE_COUNT = 4
    }

    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var imageHelper: ImageHelper

    @Inject
    lateinit var router: Router

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault())

    var images = mutableListOf<MultipartBody.Part>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        viewState.setHintText(isPublication, isPinnedEdit)
    }

    fun onPublishClicked(postId: String?, text: String) {
        if (text.isEmpty())
            return
        when {
            isPublication && !postId.isNullOrEmpty() -> updatePublication(postId, text)
            !isPublication && (isPinnedEdit == null || isPinnedEdit) -> updatePost(
                text
            )
            else -> createPost(text, images)
        }
    }

    fun addImages(newImages: List<Bitmap>) {
        if (newImages.isEmpty()) return
        val remain = max(MAX_ATTACHED_IMAGE_COUNT - images.size, 0)
        val imageCountToAdd = min(newImages.size, remain)
        repeat(imageCountToAdd) { addImage(newImages[it]) }
        viewState.showImagesAddedMessage(imageCountToAdd)
    }

    private fun addImage(imageBitmap: Bitmap) {
        val stream = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        images.add(
            MultipartBody.Part.createFormData(
                "image[${images.size}]", "photo${dateFormat.format(Date())}",
                byteArray.toRequestBody("image/*".toMediaTypeOrNull(), 0, byteArray.size)
            )
        )
    }

    private fun updatePublication(postId: String, text: String) {
        apiRepo
            .updatePublication(profile.token!!, postId, profile.user!!.id, text)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                router.exit()
            }, {
                it.printStackTrace()
            })
    }

    private fun updatePost(text: String) {
        apiRepo
            .updatePinnedPostPatch(profile.token!!, profile.user!!.id, text)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                router.exit()
            }, {
                it.printStackTrace()
            })
    }

    private fun createPost(text: String, images: List<MultipartBody.Part>) {
        apiRepo
            .createPost(
                profile.token!!, profile.user!!.id,
                text,
                images
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                router.sendResult(NEW_POST_RESULT, it)
                router.exit()
            }, {
                it.printStackTrace()
            })
    }
}