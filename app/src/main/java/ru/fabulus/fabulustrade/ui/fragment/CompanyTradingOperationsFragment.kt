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
import ru.fabulus.fabulustrade.databinding.FragmentCompanyTradingOperationsBinding
import ru.fabulus.fabulustrade.mvp.presenter.CompanyTradingOperationsPresenter
import ru.fabulus.fabulustrade.mvp.view.CompanyTradingOperationsView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.adapter.CompanyTradingOperationsRVAdapter

class CompanyTradingOperationsFragment(private val traderId: String, private val companyId: Int) :
    MvpAppCompatFragment(), CompanyTradingOperationsView {
    private var _binding: FragmentCompanyTradingOperationsBinding? = null
    private val binding: FragmentCompanyTradingOperationsBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        fun newInstance(traderId: String, companyId: Int) =
            CompanyTradingOperationsFragment(traderId, companyId)
    }

    @InjectPresenter
    lateinit var presenter: CompanyTradingOperationsPresenter

    @ProvidePresenter
    fun providePresenter() = CompanyTradingOperationsPresenter(traderId, companyId).apply {
        App.instance.appComponent.inject(this)
    }

    private var companyTradingOperationsRVAdapter: CompanyTradingOperationsRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCompanyTradingOperationsBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun init() {
        companyTradingOperationsRVAdapter =
            CompanyTradingOperationsRVAdapter(presenter.listPresenter).apply {
                App.instance.appComponent.inject(this)
            }
        binding.rvCompTradingOps.run {
            adapter = companyTradingOperationsRVAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun updateRecyclerView() {
        companyTradingOperationsRVAdapter?.notifyDataSetChanged()
    }

    override fun setCompanyName(name: String?) {
        binding.tvCompTradingOpsName.text = name
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}