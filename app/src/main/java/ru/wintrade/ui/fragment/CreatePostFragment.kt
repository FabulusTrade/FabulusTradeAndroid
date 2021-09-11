package ru.wintrade.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.databinding.FragmentCreatePostBinding
import ru.wintrade.mvp.presenter.CreatePostPresenter
import ru.wintrade.mvp.view.CreatePostView
import ru.wintrade.ui.App
import ru.wintrade.util.IntentConstants
import ru.wintrade.util.createBitmapFromResult
import ru.wintrade.util.showLongToast

class CreatePostFragment(
    private val postId: String? = null,
    private val isPublication: Boolean,
    private val isPinnedEdit: Boolean? = null,
    private val pinnedText: String?
) :
    MvpAppCompatFragment(),
    CreatePostView {
    private var _binding: FragmentCreatePostBinding? = null
    private val binding: FragmentCreatePostBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        fun newInstance(
            postId: String?,
            isPublication: Boolean,
            isPinnedEdit: Boolean?,
            pinnedText: String?
        ) =
            CreatePostFragment(postId, isPublication, isPinnedEdit, pinnedText)
    }

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
            btnCreatePostPublish.setOnClickListener {
                presenter.onPublishClicked(postId, etCreatePost.text.toString())
            }
            btnCreatePostLoadFile.setOnClickListener {
                loadFileFromDevice()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IntentConstants.PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            val imageBitmap = data.createBitmapFromResult(requireActivity())
            presenter.setImage(imageBitmap!!)
        }
    }

    private fun loadFileFromDevice() {
        val galleryIntent =
            Intent(Intent.ACTION_PICK).apply { this.type = getString(R.string.gallery_mask) }
        val intentChooser = Intent(Intent.ACTION_CHOOSER).apply {
            this.putExtra(Intent.EXTRA_INTENT, galleryIntent)
            this.putExtra(Intent.EXTRA_TITLE, resources.getString(R.string.gallery))
        }
        startActivityForResult(intentChooser, IntentConstants.PICK_IMAGE)
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

    override fun showToast(msg: String) {
        requireContext().showLongToast(msg)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}