package ru.fabulus.fabulustrade.ui.fragment.generalfeed

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.FragmentGeneralFeedBinding
import ru.fabulus.fabulustrade.mvp.presenter.generalfeed.GeneralFeedPostPresenter
import ru.fabulus.fabulustrade.mvp.presenter.traderme.TraderMePostPresenter
import ru.fabulus.fabulustrade.mvp.view.trader.TraderMePostView
import ru.fabulus.fabulustrade.navigation.Screens
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.adapter.PostRVAdapter
import ru.fabulus.fabulustrade.ui.adapter.PostWithBlacklistRVAdapter
import ru.fabulus.fabulustrade.util.*
import javax.inject.Inject


class GeneralFeedFragment : MvpAppCompatFragment(), TraderMePostView {
    private var _binding: FragmentGeneralFeedBinding? = null
    private val binding: FragmentGeneralFeedBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        fun newInstance() = GeneralFeedFragment()
    }

    @InjectPresenter
    lateinit var presenter: GeneralFeedPostPresenter

    @ProvidePresenter
    fun providePresenter() = GeneralFeedPostPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    @Inject
    lateinit var router: Router

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            presenter.listPresenter.incRepostCount()
        }

    private var postRVAdapter: PostWithBlacklistRVAdapter? = null

    override fun init() {
        initView()
        initRecyclerView()
        initListeners()
    }

    private fun initView() {
        setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        setToolbarVisible(true)
    }

    override fun setBtnsState(state: TraderMePostPresenter.ButtonsState) {}

    fun initListeners() {
        binding.layoutPostsNotAuth.run {
            btnTraderPostEntry.setOnClickListener {
                presenter.openSignInScreen()
            }
            btnTraderPostRegistration.setOnClickListener {
                presenter.openSignUpScreen()
            }
        }
        binding.ibOpenBlacklist.setOnClickListener {
            router.navigateTo(Screens.blacklistScreen())
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun updateAdapter() {
        postRVAdapter?.notifyDataSetChanged()
    }

    override fun isAuthorized(isAuth: Boolean) {
        binding.run {
            if (!isAuth) {
                rvGeneralFeed.hide()
                layoutPostsNotAuth.layoutTraderPostNotAuth.show()
            } else {
                rvGeneralFeed.show()
                layoutPostsNotAuth.layoutTraderPostNotAuth.hide()
            }
        }
    }

    override fun detachAdapter() {
        binding.rvGeneralFeed.adapter = null
    }

    override fun attachAdapter() {
        binding.rvGeneralFeed.adapter = postRVAdapter
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
            binding.rvGeneralFeed,
            "",
            Snackbar.LENGTH_LONG
        )
    }

    override fun showQuestionToFlashDialog(onClickYes: () -> Unit) {

    }

    override fun showMessagePostIsFlashed() {
        context?.let {
            AlertDialog.Builder(it)
                .setMessage(R.string.the_post_is_posted_in_the_general_feed)
                .setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    override fun showMessageSureToAddToBlacklist(traderId: String) {
        context?.let {
            android.app.AlertDialog.Builder(it)
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

    private fun initRecyclerView() {
        binding.rvGeneralFeed.run {
            postRVAdapter = PostWithBlacklistRVAdapter(presenter.listPresenter)
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGeneralFeedBinding.inflate(inflater, container, false)
        App.instance.appComponent.inject(this)
        return _binding?.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}