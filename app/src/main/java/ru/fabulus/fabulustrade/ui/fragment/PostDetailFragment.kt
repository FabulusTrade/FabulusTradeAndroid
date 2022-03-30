package ru.fabulus.fabulustrade.ui.fragment

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.Complaint
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.presenter.PostDetailPresenter
import ru.fabulus.fabulustrade.mvp.view.PostDetailView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.adapter.CommentRVAdapter

class PostDetailFragment : BasePostFragment(), PostDetailView {

    companion object {
        const val POST_KEY = "post"
        fun newInstance(post: Post) = PostDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(POST_KEY, post)
            }
        }
    }

    @InjectPresenter
    lateinit var postDetailPresenter: PostDetailPresenter

    @ProvidePresenter
    fun providePostDetailPresenter() =
        PostDetailPresenter(requireArguments()[POST_KEY] as Post).apply {
            App.instance.appComponent.inject(this)
        }

    override fun setFlashVisibility(isVisible: Boolean) = with(binding) {
        with(incItemPostHeader) {
            if (isVisible) {
                ivFlash.visibility = View.VISIBLE
            } else {
                ivFlash.visibility = View.GONE
            }
        }
    }

    override fun initRecyclerView() {
        commentRVAdapter = CommentRVAdapter(postDetailPresenter.listPresenter)
        postBinding.rvPostComments.run {
            adapter = commentRVAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun setRepostCount(text: String) {
        postBinding.incItemPostFooter.tvRepostCount.text = text
    }

    override fun setPostMenuSelf(post: Post) {
        binding.incItemPostHeader.ivAttachedKebab.setOnClickListener { btn ->
            val menu = PopupMenu(context, btn)
            menu.inflate(R.menu.menu_self_comment)

            menu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {

                    R.id.edit_comment -> {
                        presenter.editPost()
                        return@setOnMenuItemClickListener true
                    }
                    R.id.copy_comment_text -> {
                        presenter.copyPost()
                        return@setOnMenuItemClickListener true
                    }
                    R.id.delete_comment -> {
                        presenter.deletePost()
                        return@setOnMenuItemClickListener true
                    }
                    else -> return@setOnMenuItemClickListener false
                }
            }
            menu.show()
        }
    }

    override fun setPostMenuSomeone(post: Post, complaintList: List<Complaint>) {
        binding.incItemPostHeader.ivAttachedKebab.setOnClickListener { btn ->
            val popupMenu = PopupMenu(context, btn)
            popupMenu.inflate(R.menu.menu_someone_comment)
            val complaintItem = popupMenu.menu.findItem(R.id.mi_complain_on_comment)
            complaintList.forEach { complaint ->
                complaintItem.subMenu.add(Menu.NONE, complaint.id, Menu.NONE, complaint.text)
                    .setOnMenuItemClickListener {
                        presenter.complainOnPost(complaint.id)
                        return@setOnMenuItemClickListener true
                    }
            }

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.mi_copy_comment_text -> {
                        presenter.copyPost()
                        return@setOnMenuItemClickListener true
                    }
                    else -> return@setOnMenuItemClickListener false
                }
            }
            popupMenu.show()
        }
    }

    override fun setProfitAndFollowersVisibility(isVisible: Boolean) = with(binding) {
        with(incItemPostHeader) {
            if (isVisible) {
                ivProfitArrow.visibility = View.VISIBLE
                tvProfitPercent.visibility = View.VISIBLE

                ivPersonAdd.visibility = View.VISIBLE
                tvAuthorFollowerCount.visibility = View.VISIBLE
            } else {
                ivProfitArrow.visibility = View.GONE
                tvProfitPercent.visibility = View.GONE

                ivPersonAdd.visibility = View.GONE
                tvAuthorFollowerCount.visibility = View.GONE
            }
        }
    }
}