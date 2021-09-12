package ru.wintrade.ui.fragment.traders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.databinding.FragmentTradersAllBinding
import ru.wintrade.mvp.presenter.traders.TradersAllPresenter
import ru.wintrade.mvp.view.traders.TradersAllView
import ru.wintrade.ui.App
import ru.wintrade.ui.adapter.TradersAllRVAdapter

class TradersAllFragment : MvpAppCompatFragment(), TradersAllView {
    private var _binding: FragmentTradersAllBinding? = null
    private val binding: FragmentTradersAllBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        fun newInstance() = TradersAllFragment()
    }

    private var tradersAllRVAdapter: TradersAllRVAdapter? = null

    @InjectPresenter
    lateinit var presenter: TradersAllPresenter

    @ProvidePresenter
    fun providePresenter() = TradersAllPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTradersAllBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun init() {
        binding.rvAllTraders.run {
            tradersAllRVAdapter = TradersAllRVAdapter(presenter.listPresenter)
            adapter = tradersAllRVAdapter
            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(
                object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if (dy > 0) {
                            val layoutManager = layoutManager as LinearLayoutManager
                            val visibleItemCount = layoutManager.childCount
                            val totalItemCount = layoutManager.itemCount
                            val pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()
                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                presenter.onScrollLimit()
                            }

                        }
                    }
                }
            )
        }
    }

    override fun updateAdapter() {
        tradersAllRVAdapter?.notifyDataSetChanged()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}