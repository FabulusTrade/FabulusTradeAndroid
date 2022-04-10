package ru.fabulus.fabulustrade.mvp.presenter

import android.graphics.Bitmap
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import ru.fabulus.fabulustrade.mvp.view.CreatePostView
import ru.fabulus.fabulustrade.util.ImageHelper
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

class CreatePostPresenter(
    private val post: Post?,
    private val isPublication: Boolean,
    private val isPinnedEdit: Boolean?
) : MvpPresenter<CreatePostView>() {

    companion object {
        const val NEW_POST_RESULT = "NEW_POST_RESULT"
        const val UPDATE_POST_RESULT = "UPDATE_POST_RESULT"
        const val UPDATE_POST_IN_FRAGMENT_RESULT = "UPDATE_POST_IN_FRAGMENT_RESULT"
        const val DELETE_POST_RESULT = "DELETE_POST_RESULT"
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

    private var imagesToAdd = mutableListOf<ByteArray>()

    private val imagesOnServerToDelete = mutableSetOf<String>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        viewState.setHintText(isPublication, isPinnedEdit)
        updateListOfImage()
    }

    private fun updateListOfImage(deletedImage: ImageOfPost? = null) {
        (post?.let { post ->
            post.images.map { ImageOfPost.ImageOnBack(it) }
        }
            ?.let { imageList ->
                imageList - imagesOnServerToDelete.map { ImageOfPost.ImageOnBack(it) }
            }
            ?: listOf<ImageOfPost>())
            .let { imageList ->
                imageList + imagesToAdd.map {
                    ImageOfPost.ImageOnDevice(it)
                }
            }
            .let { viewState.updateListOfImages(it, deletedImage) }
    }

    sealed class ImageOfPost {
        data class ImageOnBack(val image: String) : ImageOfPost()
        data class ImageOnDevice(val image: ByteArray) : ImageOfPost() {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as ImageOnDevice

                if (!image.contentEquals(other.image)) return false

                return true
            }

            override fun hashCode(): Int {
                return image.contentHashCode()
            }
        }
    }

    fun onPublishClicked(text: String) {
        if (text.isEmpty())
            return
        when {
            isPublication && post != null -> updatePublication(post.id.toString(), text)
            !isPublication && (isPinnedEdit == null || isPinnedEdit) -> updatePost(text)
            else -> createPost(text)
        }
    }

    fun addImages(newImages: List<Bitmap>) {
        if (newImages.isEmpty()) return
        val remain =
            max(
                MAX_ATTACHED_IMAGE_COUNT - (post?.images?.size
                    ?: 0) + imagesToAdd.size + imagesOnServerToDelete.size,
                0
            )
        val imageCountToAdd = min(newImages.size, remain)
        repeat(imageCountToAdd) { addImage(newImages[it]) }
        viewState.showImagesAddedMessage(imageCountToAdd)
    }

    private fun addImage(imageBitmap: Bitmap) {
        val stream = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        imagesToAdd.add(stream.toByteArray())
        updateListOfImage()
    }

    private fun updatePublication(
        postId: String,
        text: String
    ) {
        profile.token?.let { token ->
            profile.user?.let { user ->
                apiRepo
                    .updatePublication(
                        token = token,
                        postId = postId,
                        traderId = user.id,
                        text = text,
                        imagesOnServerToDelete = imagesOnServerToDelete,
                        imagesToAdd = imagesToAdd
                    )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ post ->
                        router.sendResult(UPDATE_POST_RESULT, post)
                        router.exit()
                    }, {
                        it.printStackTrace()
                    })
            }
        }
    }

    private fun updatePost(text: String) = profile.token?.let { token ->
        profile.user?.let { user ->
            apiRepo
                .updatePinnedPostPatch(token, user.id, text)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    router.exit()
                }, {
                    it.printStackTrace()
                })
        }
    }

    private fun createPost(text: String) {
        apiRepo
            .createPost(
                profile.token!!, profile.user!!.id,
                text,
                imagesToAdd
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ post ->
                router.sendResult(NEW_POST_RESULT, post)
                router.exit()
            }, {
                it.printStackTrace()
            })
    }

    fun markToDeleteImageOnServer(imageOfPost: ImageOfPost) {
        if (imageOfPost is ImageOfPost.ImageOnBack) {
            imagesOnServerToDelete.add(imageOfPost.image)
        } else if (imageOfPost is ImageOfPost.ImageOnDevice) {
            imagesToAdd.remove(imageOfPost.image)
        }
        updateListOfImage(imageOfPost)
    }
}