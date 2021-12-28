package ru.fabulus.fabulustrade.mvp.presenter.entrance

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.repo.ProfileRepo
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.presenter.adapter.IOnBoardListPresenter
import ru.fabulus.fabulustrade.mvp.view.entrance.OnBoardView
import ru.fabulus.fabulustrade.mvp.view.item.OnBoardItemView
import ru.fabulus.fabulustrade.navigation.Screens
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
            router.replaceScreen(Screens.tradersMainScreen(null))
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