package ru.wintrade.ui.fragment.traderme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.databinding.FragmentTraderMePostsBinding
import ru.wintrade.mvp.presenter.traderme.TraderMePostPresenter
import ru.wintrade.mvp.view.trader.TraderMePostView
import ru.wintrade.ui.App
import ru.wintrade.ui.adapter.PostRVAdapter

class TraderMePostFragment : MvpAppCompatFragment(), TraderMePostView {
    private var _binding: FragmentTraderMePostsBinding? = null
    private val binding: FragmentTraderMePostsBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        private const val POST_ITEM_VIEW_CACHE_SIZE = 10
        fun newInstance() = TraderMePostFragment()
    }

    @InjectPresenter
    lateinit var presenter: TraderMePostPresenter

    @ProvidePresenter
    fun providePresenter() = TraderMePostPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    private var postRVAdapter: PostRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTraderMePostsBinding.inflate(inflater, container, false)
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

    private fun initListeners() {
        binding.run {
            layoutTraderNewsTitle.setOnClickListener {
                presenter.onCreatePostBtnClicked()
            }
            btnTraderNewsPublication.setOnClickListener {
                presenter.publicationsBtnClicked()
            }
            btnTraderNewsSubscription.setOnClickListener {
                presenter.subscriptionBtnClicked()
            }
        }
    }

    private fun initRecyclerView() {
        binding.rvTraderMePost.run {
            setItemViewCacheSize(POST_ITEM_VIEW_CACHE_SIZE)
            postRVAdapter = PostRVAdapter(presenter.listPresenter)
            adapter = postRVAdapter
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

    override fun setBtnsState(state: TraderMePostPresenter.State) {
        when (state) {
            TraderMePostPresenter.State.PUBLICATIONS -> {
                publicationsStateInit()
            }
            TraderMePostPresenter.State.SUBSCRIPTION -> {
                subscriptionStateInit()
            }
        }
    }

    private fun subscriptionStateInit() {
        binding.run {
            isActive(btnTraderNewsSubscription)
            isNotActive(btnTraderNewsPublication)
            layoutTraderNewsTitle.visibility = View.GONE

        }
    }

    private fun publicationsStateInit() {
        binding.run {
            isActive(btnTraderNewsPublication)
            isNotActive(btnTraderNewsSubscription)
            layoutTraderNewsTitle.visibility = View.VISIBLE
        }
    }

    private fun isNotActive(inactiveBtn: MaterialButton) {
        inactiveBtn.apply {
            backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.colorWhite)
            setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.colorGray))
        }
    }

    private fun isActive(activeBtn: MaterialButton) {
        activeBtn.apply {
            backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.colorLightGreen)
            setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.colorPrimary))
        }
    }

    override fun updateAdapter() {
        postRVAdapter?.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}