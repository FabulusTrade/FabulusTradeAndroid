package ru.fabulus.fabulustrade.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.fabulus.fabulustrade.mvp.presenter.CreatePostPresenter

@StateStrategyType(AddToEndStrategy::class)
interface CreatePostView : MvpView {
    fun init()
    fun setHintText(isPublication: Boolean, isPinnedEdit: Boolean?)
    fun showImagesAddedMessage(count: Int)
    fun updateListOfImages(images: List<CreatePostPresenter.ImageOfPost>)
}