package ru.fabulus.fabulustrade.mvp.presenter.adapter

interface IPostWithBlacklistRVListPresenter : IPostRVListPresenter {
    fun askToAddToBlacklist(traderId: String)
    fun addToBlacklist(traderId: String)
}