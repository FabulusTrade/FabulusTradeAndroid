package ru.fabulus.fabulustrade.ui.adapter


import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.github.terrakok.cicerone.Router
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.ItemTraderNewsBinding
import ru.fabulus.fabulustrade.mvp.model.entity.Complaint
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.presenter.adapter.IPostRVListPresenter
import ru.fabulus.fabulustrade.mvp.presenter.adapter.ITraderMePostRVListPresenter
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

open class PostRVAdapter(private val presenter: IPostRVListPresenter) :
    RecyclerView.Adapter<PostRVAdapter.PostViewHolder>() {

    companion object {
        const val MAX_LINES = 5000
        const val MIN_LINES = 5
    }

    lateinit var binding: ItemTraderNewsBinding

    @Inject
    lateinit var router: Router

    init {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        binding =
            ItemTraderNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = PostViewHolder(binding)
        initListeners(binding, holder)
        return holder
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.pos = position
        presenter.bind(holder)
    }

    override fun onViewRecycled(holder: PostViewHolder) {
        super.onViewRecycled(holder)
        holder.recycle()
    }

    private fun initListeners(binding: ItemTraderNewsBinding, holder: PostViewHolder) =
        with(binding) {
            with(incItemPostFooter) {
                btnLike.setOnClickListener {
                    presenter.postLiked(holder)
                }
                btnDislike.setOnClickListener {
                    presenter.postDisliked(holder)
                }
                btnShare.setOnClickListener {
                    presenter.share(holder, imageGroup.getImageViews())
                }
            }
            btnItemTraderNewsShowText.setOnClickListener {
                presenter.setPublicationTextMaxLines(holder)
            }
            btnItemTraderNewsShowComments.setOnClickListener {
                presenter.showCommentDetails(holder)
            }
            incItemPostHeader.root.setOnClickListener {
                presenter.navigateToTraderScreen(holder.pos)
            }
            if (presenter is ITraderMePostRVListPresenter) {
                incItemPostHeader.ivFlash.setOnClickListener {
                    presenter.toFlash(holder)
                }
            }
        }

    override fun getItemCount(): Int = presenter.getCount()

    open fun inflateMenu(popupMenu: PopupMenu) {
        popupMenu.inflate(R.menu.menu_someone_post)
    }

    open fun setPopupMenuForSomeone(post: Post, popupMenu: PopupMenu) {
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.mi_copy_post_text -> {
                    presenter.copyPost(post)
                    return@setOnMenuItemClickListener true
                }
                else -> return@setOnMenuItemClickListener false
            }
        }
    }

    inner class PostViewHolder(private val binding: ItemTraderNewsBinding) :
        RecyclerView.ViewHolder(binding.root),
        PostItemView {
        override var pos: Int = -1
        override var isOpen: Boolean = false
        override var countPostTextLine: Int = -1

        private val imageLoader = ImageLoaderImpl(R.drawable.image_view_group_image_placeholder)

        init {
            binding.imageGroup.apply {
                setImageLoader(imageLoader)
                setListener { position, _ ->
                    router.navigateTo(Screens.imageBrowsingFragment(getImages(), position))
                }
            }
        }

        override fun setNewsDate(date: Date) {
            binding.incItemPostHeader.tvDate.text = date.toStringFormat()
        }

        override fun setProfit(profit: String, textColor: Int) {
            binding.incItemPostHeader.tvProfitPercent.setTextAndColor(profit, textColor)
        }

        override fun setProfitNegativeArrow() {
            binding.incItemPostHeader.ivProfitArrow.setImageResource(R.drawable.ic_profit_arrow_down)
        }

        override fun setProfitPositiveArrow() {
            binding.incItemPostHeader.ivProfitArrow.setImageResource(R.drawable.ic_profit_arrow_up)
        }

        override fun setAuthorFollowerCount(text: String) {
            binding.incItemPostHeader.tvAuthorFollowerCount.text = text
        }

        override fun setPost(text: String) {
            binding.tvItemTraderNewsPost.text = text
        }

        override fun setLikesCount(likes: Int) {
            binding.incItemPostFooter.tvLikeCount.text = likes.toString()
        }

        override fun setDislikesCount(dislikesCount: Int) {
            binding.incItemPostFooter.tvDislikeCount.text = dislikesCount.toString()
        }

        override fun setImages(images: List<String>?) {
            with(binding.imageGroup) {
                visibilityByCondition { !images.isNullOrEmpty() }
                images?.let { setImages(it) }
            }
        }

        override fun setLikeImage(isLiked: Boolean) =
            with(binding.incItemPostFooter.btnLike) {
                if (isLiked) {
                    setImageResource(R.drawable.ic_like)
                } else {
                    setImageResource(R.drawable.ic_like_inactive)
                }
            }

        override fun setDislikeImage(isDisliked: Boolean) =
            with(binding.incItemPostFooter.btnDislike) {
                if (isDisliked) {
                    setImageResource(R.drawable.ic_dislike)
                } else {
                    setImageResource(R.drawable.ic_dislike_inactive)
                }
            }

        override fun setIvAttachedKebabMenuSelf(post: Post) {
            binding.incItemPostHeader.ivAttachedKebab.setOnClickListener { btn ->
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

        override fun setIvAttachedKebabMenuSomeone(post: Post, complaintList: List<Complaint>) {
            binding.incItemPostHeader.ivAttachedKebab.setOnClickListener { btn ->
                val popupMenu = PopupMenu(itemView.context, btn)
                inflateMenu(popupMenu)
                val complaintItem = popupMenu.menu.findItem(R.id.mi_complain_on_post)
                complaintList.forEach { complaint ->
                    complaintItem.subMenu.add(Menu.NONE, complaint.id, Menu.NONE, complaint.text)
                        .setOnMenuItemClickListener {
                            presenter.complainOnPost(post, complaint.id)
                            return@setOnMenuItemClickListener true
                        }
                }

                setPopupMenuForSomeone(post, popupMenu)

                popupMenu.show()
            }
        }

        override fun setFlashVisibility(isVisible: Boolean) {
            binding.incItemPostHeader.ivFlash.visibilityByCondition { isVisible }
        }

        override fun initFlashFooterVisibility(isVisible: Boolean) {
            binding.incItemPostFooter.ivFlash.visibilityByCondition { isVisible }
        }

        override fun setFlashColor(color: Int) {
            binding.incItemPostHeader.ivFlash.setColorFilter(color)
        }

        override fun initFlashFooterColor(color: Int) {
            binding.incItemPostFooter.ivFlash.setColorFilter(color)
        }

        override fun setProfitAndFollowersVisibility(isVisible: Boolean): Unit =
            with(binding.incItemPostHeader) {
                ivPersonAdd.visibilityByCondition { isVisible }
                tvAuthorFollowerCount.visibilityByCondition { isVisible }

                ivProfitArrow.visibilityByCondition { isVisible }
                tvProfitPercent.visibilityByCondition { isVisible }
            }

        override fun getCountLineAndSetButtonVisibility(): Unit = with(binding) {
            tvItemTraderNewsPost.maxLines = MAX_LINES
            tvItemTraderNewsPost.post {
                countPostTextLine = tvItemTraderNewsPost.lineCount
                if (countPostTextLine > MIN_LINES) {
                    btnItemTraderNewsShowText.visibility = View.VISIBLE
                } else {
                    btnItemTraderNewsShowText.visibility = View.INVISIBLE
                }
                tvItemTraderNewsPost.maxLines = MIN_LINES
            }
        }

        override fun setPublicationItemTextMaxLines(isOpen: Boolean) = with(binding) {
            if (isOpen) {
                tvItemTraderNewsPost.maxLines = MAX_LINES
                btnItemTraderNewsShowText.text =
                    itemView.context.resources.getText(R.string.hide_postRv)
            } else {
                tvItemTraderNewsPost.maxLines = MIN_LINES
                btnItemTraderNewsShowText.text =
                    itemView.context.resources.getText(R.string.show_postRv)
            }
        }

        override fun setProfileName(profileName: String) {
            binding.incItemPostHeader.tvAuthorName.text = profileName
        }

        override fun setProfileAvatar(avatarUrlPath: String) {
            loadImage(avatarUrlPath, binding.incItemPostHeader.ivAuthorAvatar)
        }

        override fun setCommentCount(text: String) {
            binding.btnItemTraderNewsShowComments.text = text
        }

        override fun setRepostCount(text: String) {
            binding.incItemPostFooter.tvRepostCount.text = text
        }

        fun recycle() {
            imageLoader.clear()
        }
    }
}