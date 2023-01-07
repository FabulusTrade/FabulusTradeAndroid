package ru.fabulus.fabulustrade.mvp.presenter.adapter

import ru.fabulus.fabulustrade.mvp.presenter.CreatePostPresenter

interface IImageListPresenter {
    fun markToDeleteImageOnServer(imageOfPost: CreatePostPresenter.ImageOfPost)
}