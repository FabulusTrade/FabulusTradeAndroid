package ru.wintrade.ui.fragment

import android.os.Bundle
import android.text.Editable
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
import ru.wintrade.ui.activity.MainActivity

class CreatePostFragment(val isPinnedEdit: Boolean? = null, val pinnedText: String?) :
    MvpAppCompatFragment(),
    CreatePostView {
    companion object {
        fun newInstance(isPinnedEdit: Boolean?, pinnedText: String?) =
            CreatePostFragment(isPinnedEdit, pinnedText)
    }

    @InjectPresenter
    lateinit var presenter: CreatePostPresenter

    @ProvidePresenter
    fun providePresenter() = CreatePostPresenter(isPinnedEdit).apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_create_post, container, false)

    override fun init() {
        btn_create_post_publish.setOnClickListener {
            presenter.onPublishClicked(et_create_post.text.toString())
        }

        btn_create_post_load_file.setOnClickListener {
            presenter.onUploadFileClicked()
        }
    }

    override fun pickImages() {
        (activity as MainActivity).startActivityPickImages()
    }

    override fun setHintText(isPinnedEdit: Boolean?) {
        when {
            isPinnedEdit == null -> et_create_post.text?.insert(et_create_post.selectionEnd, pinnedText)
            isPinnedEdit -> et_create_post.hint = "Расскажите о себе и своей успешной стратегии"
            !isPinnedEdit -> et_create_post.hint =
                "Поделитесь своими мыслями. Используйте \$ для инструментов или # для новости"
        }
    }
}