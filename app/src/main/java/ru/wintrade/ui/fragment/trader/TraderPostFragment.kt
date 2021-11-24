package ru.wintrade.ui.fragment.trader

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
import ru.wintrade.databinding.FragmentTraderPostBinding
import ru.wintrade.mvp.model.entity.Trader
import ru.wintrade.mvp.presenter.trader.TraderPostPresenter
import ru.wintrade.mvp.view.trader.TraderPostView
import ru.wintrade.ui.App
import ru.wintrade.ui.adapter.PostRVAdapter
import ru.wintrade.ui.adapter.divider.RecyclerViewItemDecoration

class TraderPostFragment : MvpAppCompatFragment(), TraderPostView {
    private var _binding: FragmentTraderPostBinding? = null
    private val binding: FragmentTraderPostBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        private const val POST_ITEM_VIEW_CACHE_SIZE = 10
        const val TRADER = "trader"
        fun newInstance(trader: Trader) =
            TraderPostFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(TRADER, trader)
                }
            }
    }

    @InjectPresenter
    lateinit var presenter: TraderPostPresenter

    @ProvidePresenter
    fun providePresenter() = TraderPostPresenter(
        arguments?.get(TRADER) as Trader
    ).apply {
        App.instance.appComponent.inject(this)
    }

    private var postRVAdapter: PostRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTraderPostBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun init() {
        initRV()
        initListeners()
    }

    private fun initRV() {
        postRVAdapter = PostRVAdapter(presenter.listPresenter)
        binding.rvTraderPost.run {
            setItemViewCacheSize(POST_ITEM_VIEW_CACHE_SIZE)
            adapter = postRVAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                RecyclerViewItemDecoration(requireContext(), R.drawable.divider_rv_horizontal)
            )
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

    fun initListeners() {
        binding.run {
            btnTraderPostEntry.setOnClickListener {
                presenter.openSignInScreen()
            }
            btnTraderPostRegistration.setOnClickListener {
                presenter.openSignUpScreen()
            }
        }
    }

    override fun updateAdapter() {
        postRVAdapter?.notifyDataSetChanged()
    }

    override fun isAuthorized(isAuth: Boolean) {
        binding.run {
            if (!isAuth) {
                layoutTraderPostIsAuth.visibility = View.GONE
                layoutTraderPostNotAuth.visibility = View.VISIBLE
            } else {
                layoutTraderPostIsAuth.visibility = View.VISIBLE
                layoutTraderPostNotAuth.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}