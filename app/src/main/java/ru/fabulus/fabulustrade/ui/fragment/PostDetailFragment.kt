package ru.fabulus.fabulustrade.ui.fragment

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.Complaint
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.presenter.BasePostPresenter
import ru.fabulus.fabulustrade.mvp.presenter.PostDetailPresenter
import ru.fabulus.fabulustrade.mvp.view.BasePostView
import ru.fabulus.fabulustrade.mvp.view.PostDetailView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.adapter.CommentRVAdapter

class PostDetailFragment : BasePostFragment(), PostDetailView {

    companion object {
        const val POST_KEY = "post"
        const val NAVIGATE_KEY = "navigate"
        fun newInstance(post: Post, navigateFromGeneralFeed: Boolean) = PostDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(POST_KEY, post)
                putBoolean(NAVIGATE_KEY, navigateFromGeneralFeed)
            }
        }
    }

    @InjectPresenter
    lateinit var postDetailPresenter: PostDetailPresenter

    override lateinit var presenter: BasePostPresenter<BasePostView>

    @ProvidePresenter
    fun providePostDetailPresenter() =
        PostDetailPresenter(requireArguments()[POST_KEY] as Post).apply {
            App.instance.appComponent.inject(this)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(context, this::class.java.simpleName, Toast.LENGTH_SHORT).show()
        presenter = postDetailPresenter as BasePostPresenter<BasePostView>
    }

    override fun setHeaderFlashVisibility(isVisible: Boolean) {
        with(binding.incItemPostHeader) {
            if (isVisible) {
                ivFlash.visibility = View.VISIBLE
            } else {
                ivFlash.visibility = View.GONE
            }
        }
    }

    override fun setHeaderFlashColor(isFlashed: Boolean) {
        with(binding.incItemPostHeader) {
            if (isVisible) {
                ivFlash.setColorFilter(requireContext().resources.getColor(R.color.colorGreen_27))
            } else {
                ivFlash.setColorFilter(requireContext().resources.getColor(R.color.colorGreen))
            }
        }
    }

    override fun initFooterFlash(visible: Boolean) {
        val navigateFromGeneralFeed: Boolean = arguments?.getBoolean(NAVIGATE_KEY) ?: false
        with(binding.incPostLayout.incItemPostFooter) {
            when(navigateFromGeneralFeed){
                true -> {
                    ivFlash.visibility = View.GONE
                }
                false -> {
                    if (visible) {
                        ivFlash.visibility = View.VISIBLE
                        ivFlash.setColorFilter(requireContext().resources.getColor(R.color.colorGreen))
                    } else {
                        ivFlash.visibility = View.GONE
                    }
                }
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
                        postDetailPresenter.editPost()
                        return@setOnMenuItemClickListener true
                    }
                    R.id.copy_comment_text -> {
                        postDetailPresenter.copyPost()
                        return@setOnMenuItemClickListener true
                    }
                    R.id.delete_comment -> {
                        postDetailPresenter.deletePost()
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
            val complaintItem = popupMenu.menu.findItem(R.id.mi_complain_on_post)
            complaintList.forEach { complaint ->
                complaintItem.subMenu.add(Menu.NONE, complaint.id, Menu.NONE, complaint.text)
                    .setOnMenuItemClickListener {
                        postDetailPresenter.complainOnPost(complaint.id)
                        return@setOnMenuItemClickListener true
                    }
            }

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.mi_copy_post_text -> {
                        postDetailPresenter.copyPost()
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