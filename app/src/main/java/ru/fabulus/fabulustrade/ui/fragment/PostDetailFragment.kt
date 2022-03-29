package ru.fabulus.fabulustrade.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.view.PostDetailView

class PostDetailFragment : BasePostFragment(), PostDetailView {

    companion object {
        const val POST_KEY = "post"
        fun newInstance(post: Post) = PostDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(POST_KEY, post)
            }
        }
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

    override fun setPostMenuSomeone(post: Post) {
        binding.incItemPostHeader.ivAttachedKebab.setOnClickListener { btn ->
            val menu = PopupMenu(context, btn)
            menu.inflate(R.menu.menu_someone_comment)

            menu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.mi_copy_comment_text -> {
                        presenter.copyPost()
                        return@setOnMenuItemClickListener true
                    }
                    R.id.mi_unethical_content,
                    R.id.mi_mat_insults_provocation,
                    R.id.mi_threats_harassment,
                    R.id.mi_market_manipulation,
                    R.id.mi_advertising,
                    R.id.mi_flood_spam,
                    R.id.mi_begging_extortion -> {
                        presenter.complainOnPost(menuItem.title.toString())
                        return@setOnMenuItemClickListener true
                    }
                    else -> return@setOnMenuItemClickListener false
                }
            }
            menu.show()
        }
    }

    override fun setProfitAndFollowersVisibility(isVisible: Boolean) = with(binding) {
        with(incItemPostHeader){
            if(isVisible){
                ivProfitArrow.visibility = View.VISIBLE
                tvProfitPercent.visibility = View.VISIBLE

                ivPersonAdd.visibility = View.VISIBLE
                tvAuthorFollowerCount.visibility = View.VISIBLE
            }else{
                ivProfitArrow.visibility = View.GONE
                tvProfitPercent.visibility = View.GONE

                ivPersonAdd.visibility = View.GONE
                tvAuthorFollowerCount.visibility = View.GONE
            }
        }
    }
}