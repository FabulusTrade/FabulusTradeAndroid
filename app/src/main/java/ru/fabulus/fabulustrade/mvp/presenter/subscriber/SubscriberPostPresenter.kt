package ru.fabulus.fabulustrade.mvp.presenter.subscriber

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.widget.ImageView
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.repo.ApiRepo
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.presenter.adapter.PostRVListPresenter
import ru.fabulus.fabulustrade.mvp.view.item.PostItemView
import ru.fabulus.fabulustrade.mvp.view.subscriber.SubscriberNewsView
import ru.fabulus.fabulustrade.navigation.Screens
import ru.fabulus.fabulustrade.util.MAX_SHARED_LEN_POST_TEXT
import ru.fabulus.fabulustrade.util.formatQuantityString
import ru.fabulus.fabulustrade.util.formatString
import ru.fabulus.fabulustrade.util.getBitmapUriFromDrawable
import javax.inject.Inject

class SubscriberPostPresenter : MvpPresenter<SubscriberNewsView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var apiRepo: ApiRepo

    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var resourceProvider: ResourceProvider

    private var isLoading = false
    private var nextPage: Int? = 1

    val listPresenter = TraderRVListPresenter()

    inner class TraderRVListPresenter : PostRVListPresenter {
        val postList = mutableListOf<Post>()

        override fun getCount(): Int = postList.size

        override fun bind(view: PostItemView) {
            val post = postList[view.pos]
            initView(view, post)
        }

        override fun share(position: Int, imageViewIdList: List<ImageView>) {
            var bmpUri: Uri? = null
            val post = postList[position]
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"

                var title = resourceProvider.formatString(
                    R.string.share_message_pattern,
                    post.userName,
                    post.text
                )

                if (title.length > MAX_SHARED_LEN_POST_TEXT) {
                    title = resourceProvider.formatString(
                        R.string.share_message_pattern_big_text,
                        title.substring(0, MAX_SHARED_LEN_POST_TEXT)
                    )
                }

                putExtra(Intent.EXTRA_TEXT, title)
                if (post.images.count() > 0) {
                    imageViewIdList[0].getBitmapUriFromDrawable()?.let { uri ->
                        bmpUri = uri
                        putExtra(Intent.EXTRA_STREAM, bmpUri)
                        type = "image/*"
                    }
                }
            }

            val chooser = Intent.createChooser(
                shareIntent,
                resourceProvider.getStringResource(R.string.share_message_title)
            )

            bmpUri?.let { uri ->
                imageViewIdList[0].context.let { context ->
                    val resInfoList: List<ResolveInfo> = context.packageManager
                        .queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)

                    for (resolveInfo in resInfoList) {
                        val packageName = resolveInfo.activityInfo.packageName
                        context.grantUriPermission(
                            packageName,
                            uri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                    }
                }

            }

            viewState.share(chooser)

        }

        private fun initView(view: PostItemView, post: Post) {
            with(view) {
                setNewsDate(post.dateCreate)
                setPost(post.text)
                setImages(post.images)
                setLikeImage(post.isLiked)
                setDislikeImage(post.isDisliked)
                setLikesCount(post.likeCount)
                setDislikesCount(post.dislikeCount)
                setKebabMenuVisibility(yoursPublication(post))
                setProfileName(post.userName)
                setProfileAvatar(post.avatarUrl)
                val commentCount = post.commentCount()
                setCommentCount(
                    resourceProvider.formatQuantityString(
                        R.plurals.show_comments_count_text,
                        commentCount,
                        commentCount
                    )
                )
            }
        }

        private fun yoursPublication(post: Post): Boolean {
            return post.traderId == profile.user?.id
        }

        override fun postLiked(view: PostItemView) {
            val post = postList[view.pos]

            apiRepo
                .likePost(profile.token!!, post.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    post.like()
                    if (post.isDisliked) {
                        view.setDislikeImage(!post.isDisliked)
                        view.setDislikesCount(post.dislikeCount - 1)
                    }
                    view.setLikesCount(post.likeCount)
                    view.setLikeImage(post.isLiked)
                }, {
                    // Ошибка не обрабатывается
                })
        }

        override fun postDisliked(view: PostItemView) {
            val post = postList[view.pos]

            apiRepo
                .dislikePost(profile.token!!, post.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    post.dislike()
                    if (post.isLiked) {
                        view.setLikeImage(!post.isLiked)
                        view.setLikesCount(post.likeCount - 1)
                    }
                    view.setDislikesCount(post.dislikeCount)
                    view.setDislikeImage(post.isDisliked)
                }, {
                    // Ошибка не обрабатывается
                })
        }

        override fun postDelete(view: PostItemView) {
            //nothing
        }

        override fun postUpdate(view: PostItemView) {
            //nothing
        }

        override fun setPublicationTextMaxLines(view: PostItemView) {
            view.isOpen = !view.isOpen
            view.setPublicationItemTextMaxLines(view.isOpen)
        }

        override fun showCommentDetails(view: PostItemView) {
            router.navigateTo(Screens.postDetailFragment(postList[view.pos]))
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun onViewResumed() {
        listPresenter.postList.clear()
        nextPage = 1
        loadPosts()
    }

    fun onScrollLimit() {
        loadPosts()
    }

    private fun loadPosts() {
        if (nextPage != null && !isLoading) {
            isLoading = true

            apiRepo
                .getPostsFollowerAndObserving(profile.token!!, nextPage!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ pag ->
                    if (pag.results.isEmpty()) {
                        viewState.withoutSubscribeAnyTrader()
                        return@subscribe
                    }
                    listPresenter.postList.addAll(pag.results)
                    viewState.updateAdapter()
                    nextPage = pag.next
                    isLoading = false
                }, {
                    it.printStackTrace()
                    isLoading = false
                })
        }
    }
}