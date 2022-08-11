package ru.fabulus.fabulustrade.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
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
import ru.fabulus.fabulustrade.databinding.LayoutPostWithCommentsBinding
import ru.fabulus.fabulustrade.mvp.model.entity.Argument
import ru.fabulus.fabulustrade.mvp.model.entity.Post
import ru.fabulus.fabulustrade.mvp.presenter.BasePostPresenter
import ru.fabulus.fabulustrade.mvp.view.BasePostView
import ru.fabulus.fabulustrade.navigation.Screens
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.adapter.CommentRVAdapter
import ru.fabulus.fabulustrade.ui.customview.imagegroup.ImageLoaderImpl
import ru.fabulus.fabulustrade.util.loadImage
import ru.fabulus.fabulustrade.util.setTextAndColor
import ru.fabulus.fabulustrade.util.showCustomSnackbar
import ru.fabulus.fabulustrade.util.showToast
import javax.inject.Inject

open class BasePostFragment : MvpAppCompatFragment(), BasePostView {
    protected lateinit var postBinding: LayoutPostWithCommentsBinding

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            presenter.incRepostCount()
        }

    protected lateinit var presenter: BasePostPresenter<BasePostView>

    @ProvidePresenter
    fun providePresenter(): BasePostPresenter<BasePostView> {
        val postArgument = requireArguments()[PostDetailFragment.POST_KEY]
        if (postArgument != null) {
            return BasePostPresenter<BasePostView>(postArgument as Post).apply {
                App.instance.appComponent.inject(this)
            }
        } else {
            val argumentArgument = requireArguments()[TradeArgumentFragment.ARGUMENT_KEY]
            if (argumentArgument != null) {
                return BasePostPresenter<BasePostView>(argumentArgument as Argument).apply {
                    App.instance.appComponent.inject(this)
                }
            } else {
                return BasePostPresenter<BasePostView>().apply {
                    App.instance.appComponent.inject(this)
                }
            }
        }
    }

    @Inject
    lateinit var router: Router

    protected var commentRVAdapter: CommentRVAdapter? = null

    override fun init() {
        initListeners()
        initRecyclerView()
    }

    protected open fun initRecyclerView() {
        commentRVAdapter = CommentRVAdapter(presenter.listPresenter)
        postBinding.rvPostComments.run {
            adapter = commentRVAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun setMaxSendCommentLength(maxLength: Int) {
        with(postBinding) {
            with(incItemSendComment) {
                tilNewCommentText.counterMaxLength = maxLength
                etNewCommentText.filters =
                    arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
            }
        }
    }

    override fun setMaxUpdateCommentLength(maxLength: Int) {
        with(postBinding) {
            with(incItemUpdateComment) {
                tilUpdateCommentText.counterMaxLength = maxLength
                etUpdateCommentText.filters =
                    arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
            }
        }
    }

    private fun initListeners() {
        with(postBinding) {

            with(incItemPostFooter) {
                btnLike.setOnClickListener {
                    presenter.likePost()
                }
                btnDislike.setOnClickListener {
                    presenter.dislikePost()
                }
                btnShare.setOnClickListener {
                    presenter.share(postBinding.imageGroup.getImageViews())
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
                        after: Int,
                    ) {

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int,
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
                        after: Int,
                    ) {

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int,
                    ) {
                        presenter.changeUpdateCommentButton(s.toString())
                    }

                    override fun afterTextChanged(s: Editable?) {

                    }
                })
            }
        }
    }

    override fun setPostImages(images: List<String>?) {
        with(postBinding.imageGroup) {
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
        postBinding.incItemPostFooter.btnLike.setImageResource(R.drawable.ic_like)
    }

    override fun setLikeInactiveImage() {
        postBinding.incItemPostFooter.btnLike.setImageResource(R.drawable.ic_like_inactive)
    }

    override fun setDislikeActiveImage() {
        postBinding.incItemPostFooter.btnDislike.setImageResource(R.drawable.ic_dislike)
    }

    override fun setDislikeInactiveImage() {
        postBinding.incItemPostFooter.btnDislike.setImageResource(R.drawable.ic_dislike_inactive)
    }

    override fun setPostLikeCount(likeCount: String) {
        postBinding.incItemPostFooter.tvLikeCount.text = likeCount
    }

    override fun setPostDislikeCount(dislikeCount: String) {
        postBinding.incItemPostFooter.tvDislikeCount.text = dislikeCount
    }

    override fun share(repostIntent: Intent) {
        resultLauncher.launch(repostIntent)
    }

    override fun setCommentCount(text: String) {
        postBinding.tvCommentCount.text = text
    }

    override fun updateCommentsAdapter() {
        commentRVAdapter?.notifyDataSetChanged()
    }

    override fun notifyItemChanged(position: Int) {
        commentRVAdapter?.notifyItemChanged(position)
    }

    override fun setRvPosition(position: Int) {
        postBinding.rvPostComments.scrollToPosition(position)
    }

    override fun setCurrentUserAvatar(avatarUrl: String) {
        loadImage(avatarUrl, postBinding.incItemSendComment.ivCurrentUserAvatar)
    }

    override fun setClickableSendCommentBtn() {
        postBinding.incItemSendComment.ibSendComment.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_send_enabled)
        postBinding.incItemSendComment.ibSendComment.isClickable = true
    }

    override fun setUnclickableSendCommentBtn() {
        postBinding.incItemSendComment.ibSendComment.isClickable = false
        postBinding.incItemSendComment.ibSendComment.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_send_disabled)
    }

    override fun setClickableUpdateCommentBtn() {
        postBinding.incItemUpdateComment.ibUpdateComment.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_check_enabled)
        postBinding.incItemUpdateComment.ibUpdateComment.isClickable = true
    }

    override fun setUnclickableUpdateCommentBtn() {
        postBinding.incItemUpdateComment.ibUpdateComment.isClickable = false
        postBinding.incItemUpdateComment.ibUpdateComment.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_check_disabled)
    }

    override fun clearNewCommentText() {
        postBinding.incItemSendComment.etNewCommentText.text?.clear()
    }

    override fun prepareReplyToComment(text: Spanned, maxCommentLength: Int) {
        showSendComment(maxCommentLength)
        with(postBinding.incItemSendComment.etNewCommentText) {
            setText(text)
            requestFocus()
            setSelection(length())
        }
    }

    override fun prepareUpdateComment(text: Spanned, maxCommentLength: Int) {
        showUpdateComment(maxCommentLength)
        with(postBinding.incItemUpdateComment) {
            ivCurrentUserAvatarUpdateComment.setImageDrawable(postBinding.incItemSendComment.ivCurrentUserAvatar.drawable)
            tvEditableText.text = text
            with(etUpdateCommentText) {
                setText(text)
                requestFocus()
                setSelection(length())
            }
        }
    }

    override fun setIncItemSendCommentVisibility(visibility: Int) {
        postBinding.incItemSendComment.root.visibility = visibility
    }

    override fun setIncItemUpdateCommentVisibility(visibility: Int) {
        postBinding.incItemUpdateComment.root.visibility = visibility
    }

    override fun showToast(text: String) {
        requireContext().showToast(text)
    }

    override fun showComplainSnackBar() {
        showCustomSnackbar(
            R.layout.layout_send_complain_snackbar,
            layoutInflater,
            postBinding.incItemSendComment.ibSendComment,
            "",
            Snackbar.LENGTH_LONG
        )
    }

    override fun scrollNsvCommentViewToBottom() {
        postBinding.nsvCommentView.post { postBinding.nsvCommentView.fullScroll(View.FOCUS_DOWN) }
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

    override fun setSendEditCommentPanel(text: String, enabled: Boolean) {
        with(postBinding) {
            with(incItemSendComment) {
                llAddComment.isEnabled = enabled
                etNewCommentText.setText(text)
                etNewCommentText.isEnabled = enabled
            }
        }
    }

    override fun setPostText(text: String) {
        postBinding.tvPost.text = text
    }
}