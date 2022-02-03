package ru.fabulus.fabulustrade.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.Spanned
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
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

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            presenter.incRepostCount()
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

    override fun setMaxSendCommentLength(maxLength: Int) {
        with(binding) {
            with(incItemSendComment) {
                tilNewCommentText.counterMaxLength = maxLength
                etNewCommentText.filters = arrayOf<InputFilter>(LengthFilter(maxLength))
            }
        }
    }

    override fun setMaxUpdateCommentLength(maxLength: Int) {
        with(binding) {
            with(incItemUpdateComment) {
                tilUpdateCommentText.counterMaxLength = maxLength
                etUpdateCommentText.filters = arrayOf<InputFilter>(LengthFilter(maxLength))
            }
        }
    }

    private fun initListeners() {
        with(binding) {

            with(incItemPostFooter) {
                btnLike.setOnClickListener {
                    presenter.likePost()
                }
                btnDislike.setOnClickListener {
                    presenter.dislikePost()
                }
                btnShare.setOnClickListener {
                    presenter.share(binding.imageGroup.getImageViews())
                }
            }

            with(incItemSendComment) {
                ibSendComment.setOnClickListener {
                    etNewCommentText.text.toString().let { text ->
                        presenter.sendComment(text)
                    }
                }

                etNewCommentText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        presenter.changeSendCommentButton(s.toString())
                    }

                    override fun afterTextChanged(s: Editable?) {

                    }
                })
            }

            with(incItemUpdateComment) {
                ibUpdateComment.setOnClickListener {
                    etUpdateCommentText.text.toString().let { text ->
                        presenter.updateComment(text)
                    }
                }

                ivClose.setOnClickListener {
                    presenter.closeUpdateComment()
                }

                etUpdateCommentText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        presenter.changeUpdateCommentButton(s.toString())
                    }

                    override fun afterTextChanged(s: Editable?) {

                    }
                })
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

    override fun setPostText(text: String) {
        binding.tvPost.text = text
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
        binding.incItemPostFooter.btnLike.setImageResource(R.drawable.ic_like)
    }

    override fun setLikeInactiveImage() {
        binding.incItemPostFooter.btnLike.setImageResource(R.drawable.ic_like_inactive)
    }

    override fun setDislikeActiveImage() {
        binding.incItemPostFooter.btnDislike.setImageResource(R.drawable.ic_dislike)
    }

    override fun setDislikeInactiveImage() {
        binding.incItemPostFooter.btnDislike.setImageResource(R.drawable.ic_dislike_inactive)
    }

    override fun setPostLikeCount(likeCount: String) {
        binding.incItemPostFooter.tvLikeCount.text = likeCount
    }

    override fun setPostDislikeCount(dislikeCount: String) {
        binding.incItemPostFooter.tvDislikeCount.text = dislikeCount
    }

    override fun share(repostIntent: Intent) {
        resultLauncher.launch(repostIntent)
    }

    override fun setCommentCount(text: String) {
        binding.tvCommentCount.text = text
    }

    override fun updateCommentsAdapter() {
        commentRVAdapter?.notifyDataSetChanged()
    }

    override fun notifyItemChanged(position: Int) {
        commentRVAdapter?.notifyItemChanged(position)
    }

    override fun setRvPosition(position: Int) {
        binding.rvPostComments.scrollToPosition(position)
    }

    override fun setCurrentUserAvatar(avatarUrl: String) {
        loadImage(avatarUrl, binding.incItemSendComment.ivCurrentUserAvatar)
    }

    override fun setClickableSendCommentBtn() {
        binding.incItemSendComment.ibSendComment.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_send_enabled)
        binding.incItemSendComment.ibSendComment.isClickable = true
    }

    override fun setUnclickableSendCommentBtn() {
        binding.incItemSendComment.ibSendComment.isClickable = false
        binding.incItemSendComment.ibSendComment.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_send_disabled)
    }

    override fun setClickableUpdateCommentBtn() {
        binding.incItemUpdateComment.ibUpdateComment.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_check_enabled)
        binding.incItemUpdateComment.ibUpdateComment.isClickable = true
    }

    override fun setUnclickableUpdateCommentBtn() {
        binding.incItemUpdateComment.ibUpdateComment.isClickable = false
        binding.incItemUpdateComment.ibUpdateComment.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_check_disabled)
    }

    override fun clearNewCommentText() {
        binding.incItemSendComment.etNewCommentText.text?.clear()
    }

    override fun prepareReplyToComment(text: Spanned, maxCommentLength: Int) {
        showSendComment(maxCommentLength)
        with(binding.incItemSendComment.etNewCommentText) {
            setText(text)
            requestFocus()
            setSelection(length())
        }
    }

    override fun prepareUpdateComment(text: Spanned, maxCommentLength: Int) {
        showUpdateComment(maxCommentLength)
        with(binding.incItemUpdateComment) {
            ivCurrentUserAvatarUpdateComment.setImageDrawable(binding.incItemSendComment.ivCurrentUserAvatar.drawable)
            tvEditableText.text = text
            with(etUpdateCommentText) {
                setText(text)
                requestFocus()
                setSelection(length())
            }
        }
    }

    override fun setIncItemSendCommentVisibility(visibility: Int) {
        binding.incItemSendComment.root.visibility = visibility
    }

    override fun setIncItemUpdateCommentVisibility(visibility: Int) {
        binding.incItemUpdateComment.root.visibility = visibility
    }

    override fun showToast(text: String) {
        requireContext().showToast(text)
    }

    override fun showComplainSnackBar() {
        showCustomSnackbar(
            R.layout.layout_send_complain_snackbar,
            layoutInflater,
            binding.incItemSendComment.ibSendComment,
            "",
            Snackbar.LENGTH_LONG
        )
    }

    override fun scrollNsvCommentViewToBottom() {
        binding.nsvCommentView.post { binding.nsvCommentView.fullScroll(View.FOCUS_DOWN) }
    }

    override fun setAuthorFollowerCount(text: String) {
        binding.incItemPostHeader.tvAuthorFollowerCount.text = text
    }

    override fun showSendComment(maxCommentLength: Int) {
        setMaxSendCommentLength(maxCommentLength)
        setIncItemUpdateCommentVisibility(View.GONE)
        setIncItemSendCommentVisibility(View.VISIBLE)
    }

    override fun showUpdateComment(maxCommentLength: Int) {
        setMaxUpdateCommentLength(maxCommentLength)
        setIncItemSendCommentVisibility(View.GONE)
        setIncItemUpdateCommentVisibility(View.VISIBLE)
    }

    override fun setRepostCount(text: String) {
        binding.incItemPostFooter.tvRepostCount.text = text
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}