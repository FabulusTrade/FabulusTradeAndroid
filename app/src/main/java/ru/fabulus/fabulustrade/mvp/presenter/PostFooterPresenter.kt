package ru.fabulus.fabulustrade.mvp.presenter

import android.widget.ImageView

interface PostFooterPresenter {

//    fun setLikeCount(likeCount: String)
//    fun setLikeActiveImage()
//    fun setLikeInactiveImage()
//
//    fun setDislikeCount(dislikeCount: String)
//    fun setDislikeActiveImage()
//    fun setDislikeInactiveImage()

    fun share(position: Int, imageViewIdList: List<ImageView>)

}