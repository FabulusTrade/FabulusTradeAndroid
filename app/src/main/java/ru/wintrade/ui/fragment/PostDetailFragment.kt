package ru.wintrade.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.databinding.FragmentPostDetailBinding
import ru.wintrade.mvp.model.entity.Post
import ru.wintrade.mvp.presenter.PostDetailPresenter
import ru.wintrade.mvp.view.PostDetailView
import ru.wintrade.ui.App
import ru.wintrade.util.loadImage

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
        return _binding?.root
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

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}