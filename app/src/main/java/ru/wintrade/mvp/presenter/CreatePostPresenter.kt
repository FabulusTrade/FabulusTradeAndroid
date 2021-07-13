package ru.wintrade.mvp.presenter

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.view.CreatePostView
import ru.wintrade.util.ImageHelper
import ru.wintrade.util.RouterResultConstants
import javax.inject.Inject

class CreatePostPresenter(val isPublication: Boolean, val isPinnedEdit: Boolean?) :
    MvpPresenter<CreatePostView>() {
    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var imageHelper: ImageHelper

    @Inject
    lateinit var router: Router

    var images = listOf<String>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        viewState.setHintText(isPinnedEdit)
    }

    fun onPublishClicked(postId: String?, text: String) {
        if (text.isEmpty())
            return
        when {
            isPublication && !postId.isNullOrEmpty() -> updatePublication(postId, text)
            !isPublication && (isPinnedEdit == null || isPinnedEdit) -> updatePost(text)
            else -> createPost(text)
        }
    }

    fun onUploadFileClicked() {
        router.setResultListener(RouterResultConstants.PICKED_IMAGES) { data ->
            images = (data as List<String>)
        }
        viewState.pickImages()
    }

    private fun updatePublication(postId: String, text: String) {
        apiRepo.updatePublication(profile.token!!, postId, profile.user!!.id, text)
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                router.exit()
            }, {})
    }

    private fun updatePost(text: String) {
        apiRepo.updatePinnedPostPatch(profile.token!!, profile.user!!.id, text)
            .observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    router.exit()
                },
                {
                    it.printStackTrace()
                }
            )
    }

    private fun createPost(text: String) {
        apiRepo.createPost(profile.token!!, profile.user!!.id, text, getImagesPairs())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    router.exit()
                },
                {
                    it.printStackTrace()
                }
            )
    }

    private fun getImagesPairs(): List<Pair<String?, ByteArray>> {
        val imagesParts = mutableListOf<Pair<String?, ByteArray>>()
        images.forEach {
            imagesParts.add(imageHelper.getBytesAndFileNameByUri(it))
        }
        return imagesParts
    }
}