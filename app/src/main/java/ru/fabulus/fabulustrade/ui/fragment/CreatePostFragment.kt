package ru.fabulus.fabulustrade.ui.fragment

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.jakewharton.rxbinding4.view.clicks
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.FragmentCreatePostBinding
import ru.fabulus.fabulustrade.mvp.presenter.CreatePostPresenter
import ru.fabulus.fabulustrade.mvp.view.CreatePostView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.util.showLongToast

class CreatePostFragment(
    private val postId: String? = null,
    private val isPublication: Boolean,
    private val isPinnedEdit: Boolean? = null,
    private val pinnedText: String?
) :
    MvpAppCompatFragment(), CreatePostView {

    companion object {
        fun newInstance(
            postId: String?,
            isPublication: Boolean,
            isPinnedEdit: Boolean?,
            pinnedText: String?
        ) = CreatePostFragment(postId, isPublication, isPinnedEdit, pinnedText)
    }

    private var _binding: FragmentCreatePostBinding? = null

    private val binding: FragmentCreatePostBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            presenter.addImages(uris.map { it.toBitmap() })
        }

    private fun Uri.toBitmap() =
        BitmapFactory.decodeStream(requireContext().contentResolver.openInputStream(this))

    @InjectPresenter
    lateinit var presenter: CreatePostPresenter

    @ProvidePresenter
    fun providePresenter() = CreatePostPresenter(isPublication, isPinnedEdit).apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun init() {
        initListeners()
    }

    fun initListeners() {
        binding.run {
            btnCreatePostPublish.clicks()
                .map { etCreatePost.text.toString() }
                .distinct()
                .subscribe { text -> presenter.onPublishClicked(postId, text) }
            btnCreatePostLoadFile.setOnClickListener {
                getContent.launch(getString(R.string.gallery_mask))
            }
        }
    }

    override fun setHintText(isPublication: Boolean, isPinnedEdit: Boolean?) {
        when {
            isPinnedEdit == null -> binding.etCreatePost.text?.insert(
                binding.etCreatePost.selectionEnd,
                pinnedText
            )
            isPinnedEdit -> binding.etCreatePost.hint =
                resources.getString(R.string.publication_header_text)
            isPublication || !isPinnedEdit -> binding.etCreatePost.hint =
                resources.getString(R.string.create_post_hint)
        }
    }

    override fun showImagesAddedMessage(count: Int) {
        requireContext().showLongToast(getString(R.string.images_added, count))
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}