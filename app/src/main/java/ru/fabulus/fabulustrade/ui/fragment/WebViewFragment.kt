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
import ru.fabulus.fabulustrade.databinding.FragmentWebViewBinding
import ru.fabulus.fabulustrade.mvp.presenter.WebViewPresenter
import ru.fabulus.fabulustrade.mvp.view.WebViewView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.util.setDrawerLockMode
import ru.fabulus.fabulustrade.util.setToolbarVisible

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
    }

    override fun setMainContent(url: String) {
        binding.mainWebView.loadUrl(url)
    }
}