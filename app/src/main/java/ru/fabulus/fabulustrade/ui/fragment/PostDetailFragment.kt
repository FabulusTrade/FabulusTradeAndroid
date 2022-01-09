package ru.fabulus.fabulustrade.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Spanned
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.FragmentPostDetailBinding
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.presenter.PostDetailPresenter
import ru.fabulus.fabulustrade.mvp.view.PostDetailView
import ru.fabulus.fabulustrade.navigation.Screens
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.adapter.CommentRVAdapter
import ru.fabulus.fabulustrade.ui.customview.imagegroup.ImageLoaderImpl
import ru.fabulus.fabulustrade.util.loadImage
import ru.fabulus.fabulustrade.util.setTextAndColor
import ru.fabulus.fabulustrade.util.showCustomSnackbar
import ru.fabulus.fabulustrade.util.showToast
import javax.inject.Inject

class PostDetailFragment : MvpAppCompatFragment(), PostDetailView {
    private var _binding: FragmentPostDetailBinding? = null
    private val binding: FragmentPostDetailBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        const val POST_KEY = "post"
        fun newInstance(post: Post) = PostDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(POST_KEY, post)
            }
        }
    }

    @InjectPresenter
    lateinit var presenter: PostDetailPresenter

    @Inject
    lateinit var router: Router

    @ProvidePresenter
    fun providePresenter() = PostDetailPresenter(requireArguments()[POST_KEY] as Post).apply {
        App.instance.appComponent.inject(this)
    }

    private var commentRVAdapter: CommentRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        App.instance.appComponent.inject(this)
        return _binding?.root
    }

    override fun init() {
        initListeners()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        commentRVAdapter = CommentRVAdapter(presenter.listPresenter)
        binding.rvPostComments.run {
            adapter = commentRVAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initListeners() {
        binding.btnPostLike.setOnClickListener {
            presenter.likePost()
        }
        binding.btnPostDislike.setOnClickListener {
            presenter.dislikePost()
        }
        binding.btnSharePost.setOnClickListener {
            presenter.sharePost(binding.imageGroup.getImageViews())
        }

        binding.ibSendComment.setOnClickListener {

            binding.etNewCommentText.text.toString().let { text ->
                presenter.listPresenter.recalcParentCommentId(text)
                presenter.addPostComment(
                    text,
                    presenter.getParentCommentId()
                )
            }
        }

        binding.etNewCommentText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                presenter.changeSendCommentButton(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    override fun setPostAuthorAvatar(avatarUrl: String) {
        loadImage(avatarUrl, binding.ivPostAuthorAvatar)
    }

    override fun setPostAuthorName(authorName: String) {
        binding.tvPostAuthorName.text = authorName
    }

    override fun setPostDateCreated(dateCreatedString: String) {
        binding.tvPostDateUpdate.text = dateCreatedString
    }

    override fun setPostText(text: String) {
        binding.tvPost.text = text
    }

    override fun setProfit(profit: String, textColor: Int) {
        binding.tvProfitPercent.setTextAndColor(profit, textColor)
    }

    override fun setProfitNegativeArrow() {
        binding.ivProfitArrow.setImageResource(R.drawable.ic_profit_arrow_down)
    }

    override fun setProfitPositiveArrow() {
        binding.ivProfitArrow.setImageResource(R.drawable.ic_profit_arrow_up)
    }

    override fun setPostImages(images: List<String>?) {
        with(binding.imageGroup) {
            if (images.isNullOrEmpty()) {
                visibility = View.GONE
            } else {
                setImageLoader(ImageLoaderImpl(R.drawable.image_view_group_image_placeholder))
                setListener { position, _ ->
                    router.navigateTo(Screens.imageBrowsingFragment(getImages(), position))
                }
                visibility = View.VISIBLE
                setImages(images)
            }
        }
    }

    override fun setLikeActiveImage() {
        binding.btnPostLike.setImageResource(R.drawable.ic_like)
    }

    override fun setLikeInactiveImage() {
        binding.btnPostLike.setImageResource(R.drawable.ic_like_inactive)
    }

    override fun setDislikeActiveImage() {
        binding.btnPostDislike.setImageResource(R.drawable.ic_dislike)
    }

    override fun setDislikeInactiveImage() {
        binding.btnPostDislike.setImageResource(R.drawable.ic_dislike_inactive)
    }

    override fun setPostLikeCount(likeCount: String) {
        binding.tvPostLikeCount.text = likeCount
    }

    override fun setPostDislikeCount(dislikeCount: String) {
        binding.tvPostDislikeCount.text = dislikeCount
    }

    override fun sharePost(shareIntent: Intent) {
        startActivity(shareIntent)
    }

    override fun setCommentCount(text: String) {
        binding.tvCommentCount.text = text
    }

    override fun updateCommentsAdapter() {
        commentRVAdapter?.notifyDataSetChanged()
    }

    override fun setRvPosition(position: Int) {
        binding.rvPostComments.scrollToPosition(position)
    }

    override fun setCurrentUserAvatar(avatarUrl: String) {
        loadImage(avatarUrl, binding.ivCurrentUserAvatar)
    }

    override fun setClickableSendCommentBtn() {
        binding.ibSendComment.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_send_enabled)
        binding.ibSendComment.isClickable = true
    }

    override fun setUnclickableSendCommentBtn() {
        binding.ibSendComment.isClickable = false
        binding.ibSendComment.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_send_disabled)
    }

    override fun clearNewCommentText() {
        binding.etNewCommentText.text.clear()
    }

    override fun prepareReplyToComment(text: Spanned) {
        binding.etNewCommentText.setText(text)
    }

    override fun showToast(text: String) {
        requireContext().showToast(text)
    }

    override fun showComplainSnackBar() {
        showCustomSnackbar(
            R.layout.layout_send_complain_snackbar,
            layoutInflater,
            binding.ibSendComment,
            "",
            Snackbar.LENGTH_LONG
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}