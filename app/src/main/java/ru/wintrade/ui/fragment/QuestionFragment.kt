package ru.wintrade.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_question.*
import kotlinx.android.synthetic.main.toolbar_blue.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.QuestionPresenter
import ru.wintrade.mvp.view.QuestionView
import ru.wintrade.ui.App
import ru.wintrade.util.showLongToast

class QuestionFragment : MvpAppCompatFragment(), QuestionView {
    companion object {
        fun newInstance() = QuestionFragment()
    }

    @InjectPresenter
    lateinit var presenter: QuestionPresenter

    @ProvidePresenter
    fun providePresenter() = QuestionPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_question, container, false)

    override fun init() {
        drawerSetUnlockMode()
        initListeners()
    }

    override fun setEmail(email: String) {
        et_question_mail.text?.insert(et_question_mail.selectionStart, email)
    }

    override fun showToast() {
        context?.showLongToast(resources.getString(R.string.question_success))
    }

    override fun clearField() {
        et_question_body.text?.clear()
    }

    fun initListeners() {
        btn_question_send.setOnClickListener {
            when {
                et_question_body.text?.length!! > 500 -> context?.showLongToast(
                    resources.getString(
                        R.string.question_too_long
                    )
                )
                et_question_body.text.isNullOrEmpty() -> context?.showLongToast(
                    resources.getString(
                        R.string.question_too_short
                    )
                )
                else -> presenter.sendMessage(et_question_body.text.toString())
            }
        }
    }

    private fun drawerSetUnlockMode() {
        requireActivity().drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        requireActivity().toolbar_blue.visibility = View.VISIBLE
    }
}