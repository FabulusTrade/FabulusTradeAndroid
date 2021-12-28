package ru.fabulus.fabulustrade.mvp.presenter

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import ru.fabulus.fabulustrade.mvp.view.QuestionView
import javax.inject.Inject

class QuestionPresenter : MvpPresenter<QuestionView>() {
    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var apiRepo: ApiRepo

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        profile.user?.email?.let { viewState.setEmail(it) }
    }

    fun sendMessage(msg: String) {
        apiRepo
            .sendQuestion(profile.token!!, msg)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewState.showToast()
                viewState.clearField()
            }, {
                // Ошибка не обрабатывается
            })
    }
}