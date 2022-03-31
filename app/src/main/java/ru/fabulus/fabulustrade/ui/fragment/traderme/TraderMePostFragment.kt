package ru.fabulus.fabulustrade.ui.fragment.traderme

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.FragmentTraderMePostsBinding
import ru.fabulus.fabulustrade.mvp.presenter.traderme.TraderMePostPresenter
import ru.fabulus.fabulustrade.mvp.view.trader.TraderMePostView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.BackButtonListener
import ru.fabulus.fabulustrade.ui.adapter.PostRVAdapter
import ru.fabulus.fabulustrade.util.showCustomSnackbar
import ru.fabulus.fabulustrade.util.showToast

class TraderMePostFragment : MvpAppCompatFragment(), TraderMePostView, BackButtonListener {
    private var _binding: FragmentTraderMePostsBinding? = null
    private val binding: FragmentTraderMePostsBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        fun newInstance() = TraderMePostFragment()
    }

    @InjectPresenter
    lateinit var presenter: TraderMePostPresenter

    @ProvidePresenter
    fun providePresenter() = TraderMePostPresenter().apply {
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
        _binding = FragmentTraderMePostsBinding.inflate(inflater, container, false)
        return _binding?.root
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
            ivFlash.setOnClickListener {
                presenter.flashedPostsBtnClicked()
            }
        }
    }

    private fun initRecyclerView() {
        binding.rvTraderMePost.run {
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
            binding.rvTraderMePost,
            "",
            Snackbar.LENGTH_LONG
        )
    }

    override fun showQuestionToFlashDialog(onClickYes: () -> Unit) {
        context?.let {
            AlertDialog.Builder(it)
                .setMessage(R.string.send_this_post_to_the_general_feed)
                .setPositiveButton(R.string.yes) { dialog, _ ->
                    onClickYes()
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.no) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
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

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun backClicked(): Boolean {
        if (this::presenter.isInitialized) {
            return presenter.backClicked()
        }
        return false
    }
}