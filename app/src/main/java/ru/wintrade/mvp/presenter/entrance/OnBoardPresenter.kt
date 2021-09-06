package ru.wintrade.mvp.presenter.entrance

import moxy.MvpPresenter
import com.github.terrakok.cicerone.Router
import ru.wintrade.mvp.model.entity.Profile
import ru.wintrade.mvp.model.repo.ProfileRepo
import ru.wintrade.mvp.model.resource.ResourceProvider
import ru.wintrade.mvp.presenter.adapter.IOnBoardListPresenter
import ru.wintrade.mvp.view.entrance.OnBoardView
import ru.wintrade.mvp.view.item.OnBoardItemView
import ru.wintrade.navigation.Screens
import javax.inject.Inject

class OnBoardPresenter : MvpPresenter<OnBoardView>() {

    @Inject
    lateinit var resourceProvider: ResourceProvider

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var profileRepo: ProfileRepo

    val listPresenter = OnBoardListPresenter()

    inner class OnBoardListPresenter : IOnBoardListPresenter {
        val images = mutableListOf<Int>()

        override fun getCount() = images.size

        override fun bindView(view: OnBoardItemView) {
            view.setImage(images[view.pos])
        }

        override fun onNextBtnClick(pos: Int) {
            profile.hasVisitedTutorial = true
            profileRepo.save(profile).subscribe()
            router.replaceScreen(Screens.tradersMainScreen())
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()

        val images = resourceProvider.getOnBoardImages()
        listPresenter.images.clear()
        listPresenter.images.addAll(images)
    }
}