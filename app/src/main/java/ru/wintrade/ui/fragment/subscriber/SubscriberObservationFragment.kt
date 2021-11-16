package ru.wintrade.ui.fragment.subscriber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.databinding.FragmentSubscriberObservationBinding
import ru.wintrade.mvp.presenter.subscriber.SubscriberObservationPresenter
import ru.wintrade.mvp.view.subscriber.SubscriberObservationView
import ru.wintrade.ui.App
import ru.wintrade.ui.adapter.ObservationRVAdapter

class SubscriberObservationFragment : MvpAppCompatFragment(), SubscriberObservationView {
    private var _binding: FragmentSubscriberObservationBinding? = null
    private val binding: FragmentSubscriberObservationBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    companion object {
        fun newInstance() = SubscriberObservationFragment()
    }

    @InjectPresenter
    lateinit var presenter: SubscriberObservationPresenter

    @ProvidePresenter
    fun providePresenter() = SubscriberObservationPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    private val observationRVAdapter: ObservationRVAdapter? by lazy {
        ObservationRVAdapter(presenter.listPresenter).apply {
            App.instance.appComponent.inject(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSubscriberObservationBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun init() {
        binding.rvSubObs.run {
            adapter = observationRVAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun updateAdapter() {
        observationRVAdapter?.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}