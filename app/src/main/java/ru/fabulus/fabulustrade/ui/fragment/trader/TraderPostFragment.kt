package ru.fabulus.fabulustrade.ui.fragment.trader

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.FragmentTraderPostBinding
import ru.fabulus.fabulustrade.mvp.model.entity.Trader
import ru.fabulus.fabulustrade.mvp.presenter.trader.TraderPostPresenter
import ru.fabulus.fabulustrade.mvp.view.trader.TraderPostView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.adapter.PostRVAdapter
import ru.fabulus.fabulustrade.util.showCustomSnackbar
import ru.fabulus.fabulustrade.util.showToast

class TraderPostFragment : MvpAppCompatFragment(), TraderPostView {
    private var _binding: FragmentTraderPostBinding? = null
    private val binding: FragmentTraderPostBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
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

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            presenter.listPresenter.incRepostCount()
        }

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

    fun initListeners() {
        binding.layoutPostsNotAuth.run {
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
                layoutPostsNotAuth.layoutTraderPostNotAuth.visibility = View.VISIBLE
            } else {
                layoutTraderPostIsAuth.visibility = View.VISIBLE
                layoutPostsNotAuth.layoutTraderPostNotAuth.visibility = View.GONE
            }
        }
    }

    override fun share(shareIntent: Intent) {
        resultLauncher.launch(shareIntent)
    }

    override fun showToast(msg: String) {
        requireContext().showToast(msg)
    }

    override fun showComplainSnackBar() {
        showCustomSnackbar(
            R.layout.layout_send_complain_snackbar,
            layoutInflater,
            binding.rvTraderPost,
            "",
            Snackbar.LENGTH_LONG
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}