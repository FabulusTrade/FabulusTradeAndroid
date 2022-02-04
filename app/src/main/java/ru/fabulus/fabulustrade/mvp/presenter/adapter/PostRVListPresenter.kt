package ru.fabulus.fabulustrade.mvp.presenter.adapter

import android.widget.ImageView
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.view.item.PostItemView

interface PostRVListPresenter {
    fun getCount(): Int
    fun bind(view: PostItemView)
    fun postLiked(view: PostItemView)
    fun postDisliked(view: PostItemView)
    fun deletePost(post: Post)
    fun editPost(view: PostItemView, post: Post)
    fun copyPost(post: Post)
    fun complainOnPost(post: Post, reason: String)
    fun setPublicationTextMaxLines(view: PostItemView)
    fun showCommentDetails(view: PostItemView)

    fun share(view: PostItemView, imageViewIdList: List<ImageView>)
    fun incRepostCount()
}