package ru.fabulus.fabulustrade.mvp.presenter

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.fabulus.fabulustrade.mvp.model.entity.BlacklistItem
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.presenter.adapter.IBlacklistListPresenter
import ru.fabulus.fabulustrade.mvp.view.BlacklistView
import ru.fabulus.fabulustrade.mvp.view.item.BlacklistItemView
import javax.inject.Inject

class BlacklistPresenter : MvpPresenter<BlacklistView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var resourceProvider: ResourceProvider

    var listPresenter = BlacklistListPresenter()
    var blacklistClickPos = -1

    private var isLoading = false
    private var nextPage: Int? = 1

    inner class BlacklistListPresenter : IBlacklistListPresenter {
        var users = mutableListOf<BlacklistItem>()
        override fun getCount(): Int = users.size

        override fun bind(view: BlacklistItemView) {
            val blacklistItem = users[view.pos]
            blacklistItem.userInBlacklistId.let { username -> view.setTraderName(username) }
        }

        override fun deleteFromBlacklist(traderId: String) {
            apiRepo.deleteFromBlacklist(profile.token!!, traderId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ responseAddToBlackList ->
                    viewState.updateAdapter()
                }, {
                    it.printStackTrace()
                })
        }
    }

    override fun attachView(view: BlacklistView?) {
        super.attachView(view)
        loadBlacklist()
        clearBlacklistClickPos()
    }

    private fun clearBlacklistClickPos() {
        blacklistClickPos = -1
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    private fun loadBlacklist() {
        apiRepo
            .getBlacklist(profile.token!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ pag ->
                listPresenter.users.addAll(pag.results)
                viewState.updateAdapter()
                nextPage = pag.next
            }, {
                // Ошибка не обрабатывается
            })
    }

    fun onScrollLimit() {
        if (nextPage != null && !isLoading) {
            isLoading = true
            profile.token?.let { token ->
                apiRepo
                    .getBlacklist(token, nextPage!!)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ pag ->
                        listPresenter.users.addAll(pag.results)
                        viewState.updateAdapter()
                        nextPage = pag.next
                        isLoading = false
                    }, { t ->
                        t.printStackTrace()
                        isLoading = false
                    })
            }
        }
    }
}