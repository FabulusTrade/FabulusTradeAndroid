package ru.wintrade.mvp.presenter

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import com.github.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.repo.ApiRepo
import ru.wintrade.mvp.view.CreatePostView
import ru.wintrade.util.RouterResultConstants
import javax.inject.Inject

class CreatePostPresenter(val isPinnedEdit: Boolean?) : MvpPresenter<CreatePostView>() {
    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var router: Router

    var images = listOf<String>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        viewState.setHintText(isPinnedEdit)
    }

    fun onPublishClicked(text: String) {
        if (text.isEmpty())
            return
        when {
            isPinnedEdit == null || isPinnedEdit -> updatePost(text)
            else -> createPost(text)
        }
    }

    fun onUploadFileClicked() {
        router.setResultListener(RouterResultConstants.PICKED_IMAGES) { data ->
            images = (data as MutableList<String>).take(4)
        }
        viewState.pickImages()
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
        apiRepo.createPost(profile.token!!, profile.user!!.id, text)
            .observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    router.exit()
                },
                {
                    it.printStackTrace()
                }
            )
    }
}