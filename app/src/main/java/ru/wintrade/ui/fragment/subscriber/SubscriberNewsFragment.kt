package ru.wintrade.ui.fragment.subscriber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.terrakok.cicerone.Router
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.databinding.FragmentSubscriberNewsBinding
import ru.wintrade.mvp.presenter.subscriber.SubscriberPostPresenter
import ru.wintrade.mvp.presenter.traders.TradersAllPresenter
import ru.wintrade.mvp.view.subscriber.SubscriberNewsView
import ru.wintrade.navigation.Screens
import ru.wintrade.ui.App
import ru.wintrade.ui.adapter.PostRVAdapter
import ru.wintrade.ui.adapter.divider.RecyclerViewItemDecoration
import javax.inject.Inject

class SubscriberNewsFragment : MvpAppCompatFragment(), SubscriberNewsView {
    private var _binding: FragmentSubscriberNewsBinding? = null
    private val binding: FragmentSubscriberNewsBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        fun newInstance() = SubscriberNewsFragment()
    }

    @InjectPresenter
    lateinit var presenter: SubscriberPostPresenter

    @ProvidePresenter
    fun providePresenter() = SubscriberPostPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    @Inject
    lateinit var router: Router

    private val postRVAdapter: PostRVAdapter by lazy { PostRVAdapter(presenter.listPresenter) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSubscriberNewsBinding.inflate(inflater, container, false)
        App.instance.appComponent.inject(this)
        return _binding?.root
    }

    override fun onResume() {
        super.onResume()
        presenter.onViewResumed()
    }

    override fun init() {
        initRecyclerView()
        initListeners()
    }

    private fun initRecyclerView() {
        binding.rvSubscriberNews.run {
            adapter = postRVAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                RecyclerViewItemDecoration(requireContext(), R.drawable.divider_rv_horizontal)
            )
            addOnScrollListener(
                object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if (dy > 0) {
                            val linearLayoutManager = layoutManager as LinearLayoutManager
                            val visibleItemCount = linearLayoutManager.childCount
                            val totalItemCount = linearLayoutManager.itemCount
                            val pastVisiblesItems =
                                linearLayoutManager.findFirstVisibleItemPosition()
                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                presenter.onScrollLimit()
                            }
                        }
                    }
                }
            )

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
        postRVAdapter?.notifyDataSetChanged()
    }

    override fun withoutSubscribeAnyTrader() {
        binding.layoutHasNoSubs.root.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}