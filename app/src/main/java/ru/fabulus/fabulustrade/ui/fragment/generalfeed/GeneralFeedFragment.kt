package ru.fabulus.fabulustrade.ui.fragment.generalfeed

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.FragmentGeneralFeedBinding
import ru.fabulus.fabulustrade.mvp.presenter.generalfeed.GeneralFeedPostPresenter
import ru.fabulus.fabulustrade.mvp.presenter.traderme.TraderMePostPresenter
import ru.fabulus.fabulustrade.mvp.view.trader.TraderMePostView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.adapter.PostRVAdapter
import ru.fabulus.fabulustrade.util.showCustomSnackbar
import ru.fabulus.fabulustrade.util.showToast


class GeneralFeedFragment : MvpAppCompatFragment(), TraderMePostView {
    private var _binding: FragmentGeneralFeedBinding? = null
    private val binding: FragmentGeneralFeedBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object{
        fun newInstance() = GeneralFeedFragment()
    }

    @InjectPresenter
    lateinit var presenter: GeneralFeedPostPresenter

    @ProvidePresenter
    fun providePresenter() = GeneralFeedPostPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            presenter.listPresenter.incRepostCount()
        }

    private var postRVAdapter: PostRVAdapter? = null

    override fun init() {
        initRecyclerView()
    }

    override fun setBtnsState(state: TraderMePostPresenter.ButtonsState) {

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun updateAdapter() {
        postRVAdapter?.notifyDataSetChanged()
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

    private fun initRecyclerView() {
        binding.rvGeneralFeed.run {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGeneralFeedBinding.inflate(inflater, container, false)
        return _binding?.root
    }
}