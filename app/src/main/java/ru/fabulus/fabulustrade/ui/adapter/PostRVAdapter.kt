package ru.fabulus.fabulustrade.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.github.terrakok.cicerone.Router
import kotlinx.android.synthetic.main.item_post_footer.view.*
import kotlinx.android.synthetic.main.item_post_header.view.*
import kotlinx.android.synthetic.main.item_trader_news.view.*
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.presenter.adapter.PostRVListPresenter
import ru.fabulus.fabulustrade.mvp.presenter.adapter.TraderMePostRVListPresenter
import ru.fabulus.fabulustrade.mvp.view.item.PostItemView
import ru.fabulus.fabulustrade.navigation.Screens
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.customview.imagegroup.ImageLoaderImpl
import ru.fabulus.fabulustrade.util.loadImage
import ru.fabulus.fabulustrade.util.setTextAndColor
import ru.fabulus.fabulustrade.util.toStringFormat
import ru.fabulus.fabulustrade.util.visibilityByCondition
import java.util.*
import javax.inject.Inject

class PostRVAdapter(val presenter: PostRVListPresenter) :
    RecyclerView.Adapter<PostRVAdapter.PostViewHolder>() {

    companion object {
        const val MAX_LINES = 5000
        const val MIN_LINES = 3
    }

    @Inject
    lateinit var router: Router

    init {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PostViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_trader_news, parent, false
        )
    )

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.pos = position
        presenter.bind(holder)
        initListeners(holder)
    }

    override fun onViewRecycled(holder: PostViewHolder) {
        super.onViewRecycled(holder)
        holder.recycle()
    }

    private fun initListeners(holder: PostViewHolder) {
        holder.itemView.inc_item_post_footer.btn_like.setOnClickListener {
            presenter.postLiked(holder)
        }
        holder.itemView.inc_item_post_footer.btn_dislike.setOnClickListener {
            presenter.postDisliked(holder)
        }
        holder.itemView.btn_item_trader_news_show_text.setOnClickListener {
            presenter.setPublicationTextMaxLines(holder)
        }
        holder.itemView.btn_item_trader_news_show_comments.setOnClickListener {
            presenter.showCommentDetails(holder)
        }

        holder.itemView.inc_item_post_footer.btn_share.setOnClickListener {
            presenter.share(holder, holder.itemView.image_group.getImageViews())
        }

        if (presenter is TraderMePostRVListPresenter) {
            holder.itemView.iv_flash.setOnClickListener {
                presenter.toFlash(holder)
            }
        }
    }

    override fun getItemCount(): Int = presenter.getCount()

    inner class PostViewHolder(view: View) : RecyclerView.ViewHolder(view),
        PostItemView {
        override var pos: Int = -1
        override var isOpen: Boolean = false

        private val imageLoader = ImageLoaderImpl(R.drawable.image_view_group_image_placeholder)

        init {
            itemView.image_group.apply {
                setImageLoader(imageLoader)
                setListener { position, _ ->
                    router.navigateTo(Screens.imageBrowsingFragment(getImages(), position))
                }
            }
        }

        override fun setNewsDate(date: Date) {
            itemView.inc_item_post_header.tv_date.text = date.toStringFormat()
        }

        override fun setProfit(profit: String, textColor: Int) {
            itemView.inc_item_post_header.tv_profit_percent.setTextAndColor(profit, textColor)
        }

        override fun setProfitNegativeArrow() {
            itemView.inc_item_post_header.iv_profit_arrow.setImageResource(R.drawable.ic_profit_arrow_down)
        }

        override fun setProfitPositiveArrow() {
            itemView.inc_item_post_header.iv_profit_arrow.setImageResource(R.drawable.ic_profit_arrow_up)
        }

        override fun setAuthorFollowerCount(text: String) {
            itemView.inc_item_post_header.tv_author_follower_count.text = text
        }

        override fun setPost(text: String) {
            itemView.tv_item_trader_news_post.text = text
        }

        override fun setLikesCount(likes: Int) {
            itemView.inc_item_post_footer.tv_like_count.text = likes.toString()
        }

        override fun setDislikesCount(dislikesCount: Int) {
            itemView.inc_item_post_footer.tv_dislike_count.text = dislikesCount.toString()
        }

        override fun setImages(images: List<String>?) {
            with(itemView.image_group) {
                if (images.isNullOrEmpty()) {
                    visibility = View.GONE
                } else {
                    visibility = View.VISIBLE
                    setImages(images)
                }
            }
        }

        override fun setLikeImage(isLiked: Boolean) {
            if (isLiked) {
                itemView.inc_item_post_footer.btn_like.setImageResource(R.drawable.ic_like)
            } else {
                itemView.inc_item_post_footer.btn_like.setImageResource(R.drawable.ic_like_inactive)
            }
        }

        override fun setDislikeImage(isDisliked: Boolean) {
            if (isDisliked) {
                itemView.inc_item_post_footer.btn_dislike.setImageResource(R.drawable.ic_dislike)
            } else {
                itemView.inc_item_post_footer.btn_dislike.setImageResource(R.drawable.ic_dislike_inactive)
            }
        }

        override fun setIvAttachedKebabMenuSelf(post: Post) {
            itemView.inc_item_post_header.iv_attached_kebab.setOnClickListener { btn ->
                val menu = PopupMenu(itemView.context, btn)
                menu.inflate(R.menu.menu_self_comment)

                menu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {

                        R.id.edit_comment -> {
                            presenter.editPost(this, post)
                            return@setOnMenuItemClickListener true
                        }
                        R.id.copy_comment_text -> {
                            presenter.copyPost(post)
                            return@setOnMenuItemClickListener true
                        }
                        R.id.delete_comment -> {
                            presenter.deletePost(this)
                            return@setOnMenuItemClickListener true
                        }
                        else -> return@setOnMenuItemClickListener false
                    }
                }
                menu.show()
            }
        }

        override fun setIvAttachedKebabMenuSomeone(post: Post) {
            itemView.inc_item_post_header.iv_attached_kebab.setOnClickListener { btn ->
                val menu = PopupMenu(itemView.context, btn)
                menu.inflate(R.menu.menu_someone_comment)

                menu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.mi_copy_comment_text -> {
                            presenter.copyPost(post)
                            return@setOnMenuItemClickListener true
                        }
                        R.id.mi_unethical_content,
                        R.id.mi_mat_insults_provocation,
                        R.id.mi_threats_harassment,
                        R.id.mi_market_manipulation,
                        R.id.mi_advertising,
                        R.id.mi_flood_spam,
                        R.id.mi_begging_extortion -> {
                            presenter.complainOnPost(post, menuItem.title.toString())
                            return@setOnMenuItemClickListener true
                        }
                        else -> return@setOnMenuItemClickListener false
                    }
                }
                menu.show()
            }
        }

        override fun setFlashVisibility(isVisible: Boolean) {
            itemView.iv_flash.visibilityByCondition { isVisible }
        }

        override fun setFlashColor(color: Int) {
            itemView.iv_flash.setColorFilter(color)
        }

        override fun setProfitAndFollowersVisibility(isVisible: Boolean) {
            if (isVisible) {
                itemView.iv_person_add.visibility = View.VISIBLE
                itemView.tv_author_follower_count.visibility = View.VISIBLE

                itemView.iv_profit_arrow.visibility = View.VISIBLE
                itemView.tv_profit_percent.visibility = View.VISIBLE
            } else {
                itemView.iv_person_add.visibility = View.GONE
                itemView.tv_author_follower_count.visibility = View.GONE

                itemView.iv_profit_arrow.visibility = View.GONE
                itemView.tv_profit_percent.visibility = View.GONE
            }
        }

        override fun setPublicationItemTextMaxLines(isOpen: Boolean) {
            if (isOpen) {
                itemView.tv_item_trader_news_post.maxLines = MAX_LINES
                itemView.btn_item_trader_news_show_text.text =
                    itemView.context.resources.getText(R.string.hide_postRv)
            } else {
                itemView.tv_item_trader_news_post.maxLines = MIN_LINES
                itemView.btn_item_trader_news_show_text.text =
                    itemView.context.resources.getText(R.string.show_postRv)
            }
        }

        override fun setProfileName(profileName: String) {
            itemView.inc_item_post_header.tv_author_name.text = profileName
        }

        override fun setProfileAvatar(avatarUrlPath: String) {
            loadImage(avatarUrlPath, itemView.inc_item_post_header.iv_author_avatar)
        }

        override fun setCommentCount(text: String) {
            itemView.btn_item_trader_news_show_comments.text = text
        }

        override fun setRepostCount(text: String) {
            itemView.tv_repost_count.text = text
        }

        fun recycle() {
            imageLoader.clear()
        }
    }
}