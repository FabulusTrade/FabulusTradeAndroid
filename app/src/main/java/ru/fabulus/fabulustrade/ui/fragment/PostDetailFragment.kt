package ru.fabulus.fabulustrade.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.FragmentPostDetailBinding
import ru.fabulus.fabulustrade.mvp.model.entity.Complaint
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.presenter.BasePostPresenter
import ru.fabulus.fabulustrade.mvp.presenter.PostDetailPresenter
import ru.fabulus.fabulustrade.mvp.view.BasePostView
import ru.fabulus.fabulustrade.mvp.view.PostDetailView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.adapter.CommentRVAdapter
import ru.fabulus.fabulustrade.util.loadImage
import ru.fabulus.fabulustrade.util.setTextAndColor

class PostDetailFragment : BasePostFragment(), PostDetailView {

    companion object {
        const val POST_KEY = "post"
        fun newInstance(post: Post) = PostDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(POST_KEY, post)
            }
        }
    }

    private var _binding: FragmentPostDetailBinding? = null

    private val binding: FragmentPostDetailBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

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
        presenter = postDetailPresenter as BasePostPresenter<BasePostView>
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        App.instance.appComponent.inject(this)
        postBinding = checkNotNull(_binding) { getString(R.string.binding_error) }.incPostLayout
        return _binding?.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
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

    override fun setPostAuthorAvatar(avatarUrl: String) {
        loadImage(avatarUrl, binding.incItemPostHeader.ivAuthorAvatar)
    }

    override fun setPostAuthorName(authorName: String) {
        binding.incItemPostHeader.tvAuthorName.text = authorName
    }

    override fun setPostDateCreated(dateCreatedString: String) {
        binding.incItemPostHeader.tvDate.text = dateCreatedString
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
}