package ru.wintrade.ui.fragment

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

class CreatePostFragment(val isPinnedEdit: Boolean? = null) : MvpAppCompatFragment(), CreatePostView {
    companion object {
        fun newInstance(isPinnedEdit: Boolean) = CreatePostFragment(isPinnedEdit)
    }

    @InjectPresenter
    lateinit var presenter: CreatePostPresenter

    @ProvidePresenter
    fun providePresenter() = CreatePostPresenter(isPinnedEdit!!).apply {
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
    }

    override fun setHintText(isPinnedEdit: Boolean) {
        if (isPinnedEdit)
            et_create_post.hint = "Расскажите о себе и своей успешной стратегии"
        else
            et_create_post.hint = "Поделитесь своими мыслями. Используйте \$ для инструментов или # для новости"
    }
}