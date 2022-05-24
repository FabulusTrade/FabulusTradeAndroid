package ru.fabulus.fabulustrade.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.terrakok.cicerone.Router
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.FragmentBlacklistBinding
import ru.fabulus.fabulustrade.mvp.presenter.BlacklistPresenter
import ru.fabulus.fabulustrade.mvp.presenter.traders.TradersAllPresenter
import ru.fabulus.fabulustrade.mvp.view.BlacklistView
import ru.fabulus.fabulustrade.navigation.Screens
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.adapter.BlacklistRVAdapter
import ru.fabulus.fabulustrade.util.showLongToast
import javax.inject.Inject

class BlacklistFragment : MvpAppCompatFragment(), BlacklistView {
    private var _binding: FragmentBlacklistBinding? = null
    private val binding: FragmentBlacklistBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        fun newInstance() = BlacklistFragment()
    }

    @InjectPresenter
    lateinit var presenter: BlacklistPresenter

    @ProvidePresenter
    fun providePresenter() = BlacklistPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    @Inject
    lateinit var router: Router

    private val blacklistRVAdapter: BlacklistRVAdapter? by lazy {
        BlacklistRVAdapter(presenter.listPresenter).apply {
            App.instance.appComponent.inject(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBlacklistBinding.inflate(inflater, container, false)
        App.instance.appComponent.inject(this)
        return _binding?.root
    }

    override fun init() {
        initRecyclerView()
        initListeners()
    }

    private fun initRecyclerView() {
        binding.rvSubObs.run {
            adapter = blacklistRVAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initListeners() {
        with(binding) {
            layoutHasNoSubs.tvChooseSubscribe.setOnClickListener {
                router.navigateTo(Screens.tradersAllScreen(TradersAllPresenter.DEFAULT_FILTER))
            }
        }
    }

    override fun updateAdapter() {
        blacklistRVAdapter?.notifyDataSetChanged()
    }

    override fun withoutSubscribeAnyTrader() {
        binding.layoutHasNoSubs.root.visibility = View.VISIBLE
    }

    override fun showToast(msg: String) {
        requireContext().showLongToast(msg)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}