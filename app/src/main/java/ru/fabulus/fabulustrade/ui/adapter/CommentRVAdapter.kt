package ru.fabulus.fabulustrade.ui.adapter


import android.text.Spannable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.terrakok.cicerone.Router
import kotlinx.android.synthetic.main.item_comment.view.*
import ru.fabulus.fabulustrade.R
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
        holder.itemView.btn_like_comment.setOnClickListener {
            presenter.likeComment(holder)
        }

        holder.itemView.tv_answer_to_comment.setOnClickListener {
            presenter.replyOnComment(holder)
        }
    }

    override fun getItemCount(): Int = presenter.getCount()

    inner class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view),
        CommentItemView {

        override var pos: Int = -1

        override fun setCommentText(text: Spannable) {
            itemView.tv_comment_post.text = text
        }

        override fun setCommentAuthorAvatar(avatarUrl: String) {
            loadImage(avatarUrl, itemView.iv_comment_author_avatar)
        }

        override fun setCommentAuthorUserName(userName: String) {
            itemView.tv_comment_author_name.text = userName
        }

        override fun setCommentDateText(text: String) {
            itemView.tv_comment_date.text = text
        }

        override fun setLikeCountText(text: String) {
            itemView.tv_comment_like_count.text = text
        }

        override fun setLikeImageActive() {
            itemView.btn_like_comment.setImageResource(R.drawable.ic_like)
        }

        override fun setLikeImageInactive() {
            itemView.btn_like_comment.setImageResource(R.drawable.ic_like_inactive)
        }
    }

}