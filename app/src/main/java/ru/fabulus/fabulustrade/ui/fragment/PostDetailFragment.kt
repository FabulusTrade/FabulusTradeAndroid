package ru.fabulus.fabulustrade.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.terrakok.cicerone.Router
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
import ru.fabulus.fabulustrade.ui.customview.imagegroup.ImageLoaderImpl
import ru.fabulus.fabulustrade.util.loadImage
import ru.fabulus.fabulustrade.util.setTextAndColor
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
    }

    private fun initListeners() {
        binding.btnPostLike.setOnClickListener {
            presenter.likePost()
        }
        binding.btnPostDislike.setOnClickListener {
            presenter.dislikePost()
        }
        binding.btnSharePost.setOnClickListener {
            presenter.sharePost()
        }
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

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}