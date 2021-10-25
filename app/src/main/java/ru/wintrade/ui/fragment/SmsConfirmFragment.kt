package ru.wintrade.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding4.widget.textChanges
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.databinding.FragmentSmsConfirmBinding
import ru.wintrade.mvp.presenter.SmsConfirmPresenter
import ru.wintrade.mvp.view.SmsConfirmView
import ru.wintrade.ui.App
import ru.wintrade.util.showLongToast
import java.util.concurrent.TimeUnit


class SmsConfirmFragment : MvpAppCompatFragment(), SmsConfirmView {
    private var _binding: FragmentSmsConfirmBinding? = null
    private val binding: FragmentSmsConfirmBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        private const val SMS_TIMEOUT = 1000L
        const val PHONE_KEY = "phone"
        fun newInstance(phone: String) = SmsConfirmFragment().apply {
            arguments = Bundle().apply {
                putString(PHONE_KEY, phone)
            }
        }
    }

    @InjectPresenter
    lateinit var presenter: SmsConfirmPresenter

    @ProvidePresenter
    fun providePresenter() = SmsConfirmPresenter(requireArguments()[PHONE_KEY] as String).apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSmsConfirmBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun init() {
        binding.run {
            btnSmsConfirmConfirm.setOnClickListener {
                presenter.confirmClicked()
            }
            btnSmsConfirmResend.setOnClickListener {
                presenter.resendClicked()
            }
            etSmsConfirmSms.textChanges()
                .debounce(SMS_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe(
                    {
                        presenter.codeChanged(it.toString())
                    },
                    {}
                )
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