package ru.fabulus.fabulustrade.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.FragmentCompanyTradingOperationsJournalBinding
import ru.fabulus.fabulustrade.mvp.presenter.CompanyTradingOperationsJournalPresenter
import ru.fabulus.fabulustrade.mvp.view.CompanyTradingOperationsJournalView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.adapter.CompanyTradingOperationsJournalRVAdapter

class CompanyTradingOperationsJournalFragment(private val traderId: String, private val companyId: Int) :
    MvpAppCompatFragment(), CompanyTradingOperationsJournalView {
    private var _binding: FragmentCompanyTradingOperationsJournalBinding? = null
    private val binding: FragmentCompanyTradingOperationsJournalBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        fun newInstance(traderId: String, companyId: Int) =
            CompanyTradingOperationsJournalFragment(traderId, companyId)
    }

    @InjectPresenter
    lateinit var presenter: CompanyTradingOperationsJournalPresenter

    @ProvidePresenter
    fun providePresenter() = CompanyTradingOperationsJournalPresenter(traderId, companyId).apply {
        App.instance.appComponent.inject(this)
    }

    private var companyTradingOperationsJournalRVAdapter: CompanyTradingOperationsJournalRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCompanyTradingOperationsJournalBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun init() {
        companyTradingOperationsJournalRVAdapter =
            CompanyTradingOperationsJournalRVAdapter(presenter.listPresenter).apply {
                App.instance.appComponent.inject(this)
            }
        binding.rvCompTradingOpsJournal.run {
            adapter = companyTradingOperationsJournalRVAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun updateRecyclerView() {
        companyTradingOperationsJournalRVAdapter?.notifyDataSetChanged()
    }

    override fun setCompanyName(name: String?) {
        binding.tvCompTradingOpsJournalName.text = name
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}