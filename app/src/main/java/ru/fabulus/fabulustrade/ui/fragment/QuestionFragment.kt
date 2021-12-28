package ru.fabulus.fabulustrade.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.FragmentQuestionBinding
import ru.fabulus.fabulustrade.mvp.presenter.QuestionPresenter
import ru.fabulus.fabulustrade.mvp.view.QuestionView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.util.setDrawerLockMode
import ru.fabulus.fabulustrade.util.setToolbarVisible
import ru.fabulus.fabulustrade.util.showLongToast

class QuestionFragment : MvpAppCompatFragment(), QuestionView {
    private var _binding: FragmentQuestionBinding? = null
    private val binding: FragmentQuestionBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        private const val MAX_QUESTION_LENGTH = 500
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
    ): View? {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun init() {
        initView()
        initListeners()
    }

    override fun setEmail(email: String) {
        binding.etQuestionMail.text?.insert(binding.etQuestionMail.selectionStart, email)
    }

    override fun showToast() {
        requireContext().showLongToast(resources.getString(R.string.question_success))
    }

    override fun clearField() {
        binding.etQuestionBody.text?.clear()
    }

    fun initListeners() {
        binding.btnQuestionSend.setOnClickListener {
            when {
                binding.etQuestionBody.text?.length!! > MAX_QUESTION_LENGTH -> requireContext().showLongToast(
                    resources.getString(
                        R.string.question_too_long
                    )
                )
                binding.etQuestionBody.text.isNullOrEmpty() -> requireContext().showLongToast(
                    resources.getString(
                        R.string.question_too_short
                    )
                )
                else -> presenter.sendMessage(binding.etQuestionBody.text.toString())
            }
        }
    }

    private fun initView() {
        setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        setToolbarVisible(true)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}