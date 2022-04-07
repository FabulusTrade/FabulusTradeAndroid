package ru.fabulus.fabulustrade.mvp.presenter.generalfeed

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.view.generalfeed.GeneralFeedPostView
import javax.inject.Inject

class GeneralFeedPostPresenter : MvpPresenter<GeneralFeedPostView>() {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var resourceProvider: ResourceProvider


}