package ru.wintrade.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_create_post.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.CreatePostPresenter
import ru.wintrade.mvp.view.CreatePostView
import ru.wintrade.ui.App
import ru.wintrade.util.IntentConstants
import ru.wintrade.util.createBitmapFromResult
import ru.wintrade.util.showLongToast

class CreatePostFragment(
    val postId: String? = null,
    val isPublication: Boolean,
    val isPinnedEdit: Boolean? = null,
    val pinnedText: String?
) :
    MvpAppCompatFragment(),
    CreatePostView {
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
    ): View? = inflater.inflate(R.layout.fragment_create_post, container, false)

    override fun init() {
        initListeners()
    }

    fun initListeners() {
        btn_create_post_publish.setOnClickListener {
            presenter.onPublishClicked(postId, et_create_post.text.toString())
        }
        btn_create_post_load_file.setOnClickListener {
            loadFileFromDevice()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IntentConstants.PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            val imageBitmap = data.createBitmapFromResult(requireActivity())
            presenter.setImage(imageBitmap!!)
        }
    }

    fun loadFileFromDevice() {
        val galleryIntent = Intent(Intent.ACTION_PICK).apply { this.type = "image/*" }
        val intentChooser = Intent(Intent.ACTION_CHOOSER).apply {
            this.putExtra(Intent.EXTRA_INTENT, galleryIntent)
            this.putExtra(Intent.EXTRA_TITLE, resources.getString(R.string.gallery))
        }
        startActivityForResult(intentChooser, IntentConstants.PICK_IMAGE)
    }

    override fun setHintText(isPublication: Boolean, isPinnedEdit: Boolean?) {
        when {
            isPinnedEdit == null -> et_create_post.text?.insert(
                et_create_post.selectionEnd,
                pinnedText
            )
            isPinnedEdit -> et_create_post.hint =
                resources.getString(R.string.publication_header_text)
            isPublication || !isPinnedEdit -> et_create_post.hint =
                resources.getString(R.string.create_post_hint)
        }
    }

    override fun showToast(msg: String) {
        context?.showLongToast(msg)
    }
}