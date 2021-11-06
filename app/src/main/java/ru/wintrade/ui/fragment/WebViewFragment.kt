package ru.wintrade.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.databinding.FragmentWebViewBinding
import ru.wintrade.mvp.presenter.WebViewPresenter
import ru.wintrade.mvp.view.WebViewView
import ru.wintrade.ui.App
import ru.wintrade.util.setDrawerLockMode
import ru.wintrade.util.setToolbarVisible

class WebViewFragment : MvpAppCompatFragment(), WebViewView {
    companion object {
        private const val URL_TAG = "url_tag"
        fun newInstance(url: String): WebViewFragment =
            WebViewFragment().apply {
                arguments = Bundle().apply {
                    putString(URL_TAG, url)
                }
            }
    }

    private var _binding: FragmentWebViewBinding? = null
    private val binding: FragmentWebViewBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    @InjectPresenter
    lateinit var presenter: WebViewPresenter

    @ProvidePresenter
    fun providePresenter() = WebViewPresenter(requireArguments()[URL_TAG] as String).apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWebViewBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun init() {
        setToolbarVisible(false)
        setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        initListeners()
    }

    private fun initListeners() {
        binding.btnClose.setOnClickListener {
            presenter.onCLoseClicked()
        }
    }

    override fun setMainContent(url: String) {
        binding.mainWebView.loadUrl(url)
    }
}