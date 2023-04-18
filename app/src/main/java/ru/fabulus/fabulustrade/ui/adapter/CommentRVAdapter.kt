package ru.fabulus.fabulustrade.ui.adapter


import android.text.Spannable
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.github.terrakok.cicerone.Router
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.ItemCommentBinding
import ru.fabulus.fabulustrade.mvp.model.entity.Comment
import ru.fabulus.fabulustrade.mvp.model.entity.Complaint
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.presenter.adapter.CommentRVListPresenter
import ru.fabulus.fabulustrade.mvp.view.item.CommentItemView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.util.loadImage
import javax.inject.Inject

class CommentRVAdapter(val presenter: CommentRVListPresenter) :
    RecyclerView.Adapter<CommentRVAdapter.CommentViewHolder>() {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var resourceProvider: ResourceProvider

    init {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CommentViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_comment, parent, false
        )
    )

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.pos = position
        presenter.bind(holder)
        initListeners(holder)
    }

    private fun initListeners(holder: CommentViewHolder) {
        holder.binding.btnLikeComment.setOnClickListener {
            presenter.likeComment(holder)
        }

        holder.binding.tvAnswerToComment.setOnClickListener {
            presenter.replyOnComment(holder)
        }
    }

    override fun getItemCount(): Int = presenter.getCount()

    inner class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view),
        CommentItemView {

        val binding = ItemCommentBinding.bind(itemView)

        override var pos: Int = -1

        override fun setCommentText(text: Spannable) {
            binding.tvCommentPost.text = text
        }

        override fun setCommentAuthorAvatar(avatarUrl: String) {
            loadImage(avatarUrl, binding.ivCommentAuthorAvatar)
        }

        override fun setCommentAuthorUserName(userName: String) {
            binding.tvCommentAuthorName.text = userName
        }

        override fun setCommentDateText(text: String) {
            binding.tvCommentDate.text = text
        }

        override fun setLikeCountText(text: String) {
            binding.tvCommentLikeCount.text = text
        }

        override fun setLikeImageActive() {
            binding.btnLikeComment.setImageResource(R.drawable.ic_like)
        }

        override fun setLikeImageInactive() {
            binding.btnLikeComment.setImageResource(R.drawable.ic_like_inactive)
        }

        override fun setReplyPostColor(backgroundColor: Int) {
            binding.cvCommentHeader.setCardBackgroundColor(backgroundColor)
        }

        override fun setBtnCommentMenuSelf(comment: Comment) {
            binding.btnCommentMenu.setOnClickListener { btn ->
                val menu = PopupMenu(itemView.context, btn)
                menu.inflate(R.menu.menu_self_comment)

                menu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {

                        R.id.edit_comment -> {
                            presenter.editComment(this, comment)
                            return@setOnMenuItemClickListener true
                        }
                        R.id.copy_comment_text -> {
                            presenter.copyComment(comment)
                            return@setOnMenuItemClickListener true
                        }
                        R.id.delete_comment -> {
                            presenter.deleteComment(this, comment)
                            return@setOnMenuItemClickListener true
                        }
                        else -> return@setOnMenuItemClickListener false
                    }
                }
                menu.show()
            }
        }

        override fun setBtnCommentMenuSomeone(
            comment: Comment,
            complaintList: List<Complaint>,
            showLockUserMenuItem: Boolean,
            showUnlockUserMenuItem: Boolean
        ) {
            binding.btnCommentMenu.setOnClickListener { btn ->
                val popupMenu = PopupMenu(itemView.context, btn)
                popupMenu.inflate(R.menu.menu_someone_comment)

                val complaintItem = popupMenu.menu.findItem(R.id.mi_complain_on_post)
                complaintList.forEach { complaint ->
                    complaintItem.subMenu?.add(Menu.NONE, complaint.id, Menu.NONE, complaint.text)
                        ?.setOnMenuItemClickListener {
                            presenter.complainOnComment(comment.id, complaint.id)
                            return@setOnMenuItemClickListener true
                        }
                }

                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.mi_copy_post_text -> {
                            presenter.copyComment(comment)
                            return@setOnMenuItemClickListener true
                        }
                        R.id.mi_lock_user_comment -> {
                            presenter.lockUserComment(this, comment)
                            return@setOnMenuItemClickListener true
                        }
                        R.id.mi_unlock_user_comment -> {
                            presenter.unlockUserComment(this, comment)
                            return@setOnMenuItemClickListener true
                        }
                        else -> return@setOnMenuItemClickListener false
                    }
                }
                popupMenu.menu.findItem(R.id.mi_lock_user_comment).isVisible = showLockUserMenuItem
                popupMenu.menu.findItem(R.id.mi_unlock_user_comment).isVisible = showUnlockUserMenuItem

                popupMenu.show()
            }
        }

        override fun hideReplyBtn() {
            binding.tvAnswerToComment.visibility = View.INVISIBLE
        }
    }


}