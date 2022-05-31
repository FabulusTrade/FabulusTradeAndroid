package ru.fabulus.fabulustrade.ui.fragment.subscriber

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.FragmentSubscriberNewsBinding
import ru.fabulus.fabulustrade.mvp.presenter.subscriber.SubscriberPostPresenter
import ru.fabulus.fabulustrade.mvp.presenter.traders.TradersAllPresenter
import ru.fabulus.fabulustrade.mvp.view.subscriber.SubscriberPostView
import ru.fabulus.fabulustrade.navigation.Screens
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.adapter.PostWithBlacklistRVAdapter
import ru.fabulus.fabulustrade.util.showCustomSnackbar
import ru.fabulus.fabulustrade.util.showLongToast
import ru.fabulus.fabulustrade.util.showToast
import javax.inject.Inject

class SubscriberPostFragment : MvpAppCompatFragment(), SubscriberPostView {
    private var _binding: FragmentSubscriberNewsBinding? = null
    private val binding: FragmentSubscriberNewsBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        fun newInstance() = SubscriberPostFragment()
    }

    @InjectPresenter
    lateinit var presenter: SubscriberPostPresenter

    @ProvidePresenter
    fun providePresenter() = SubscriberPostPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    @Inject
    lateinit var router: Router

    private val postRVAdapter: PostWithBlacklistRVAdapter by lazy { PostWithBlacklistRVAdapter(presenter.listPresenter) }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            presenter.listPresenter.incRepostCount()
        }

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
            ibOpenBlacklist.setOnClickListener {
                router.navigateTo(Screens.blacklistScreen())
            }
        }
    }

    override fun updateAdapter() {
        postRVAdapter.notifyDataSetChanged()
    }

    override fun withoutSubscribeAnyTrader() {
        binding.layoutHasNoSubs.root.visibility = View.VISIBLE
    }

    override fun share(shareIntent: Intent) {
        resultLauncher.launch(shareIntent)
    }

    override fun showToast(msg: String) {
        requireContext().showToast(msg)
    }

    override fun showMessageSureToAddToBlacklist(traderId: String) {
        context?.let {
            AlertDialog.Builder(it)
                .setMessage(getString(R.string.are_you_sure_to_add_to_blacklist))
                .setPositiveButton(getString(R.string.yes_exclamation)) { dialog, _ ->
                    dialog.dismiss()
                    presenter.listPresenter.addToBlacklist(traderId)
                }
                .create()
                .show()
        }
    }

    override fun showMessagePostAddedToBlacklist() {
        requireContext().showLongToast(resources.getString(R.string.added_to_blacklist))
    }

    override fun showComplainSnackBar() {
        showCustomSnackbar(
            R.layout.layout_send_complain_snackbar,
            layoutInflater,
            binding.rvSubscriberNews,
            "",
            Snackbar.LENGTH_LONG
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}