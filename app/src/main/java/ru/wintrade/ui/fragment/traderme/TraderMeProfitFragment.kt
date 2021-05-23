package ru.wintrade.ui.fragment.traderme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_trader_me_profit.*
import kotlinx.android.synthetic.main.layout_attached_post.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.wintrade.R
import ru.wintrade.mvp.presenter.traderme.TraderMeProfitPresenter
import ru.wintrade.mvp.view.traderme.TraderMeProfitView
import ru.wintrade.ui.App

class TraderMeProfitFragment : MvpAppCompatFragment(),
    TraderMeProfitView {
    companion object {
        fun newInstance() = TraderMeProfitFragment()
    }

    @InjectPresenter
    lateinit var presenter: TraderMeProfitPresenter

    @ProvidePresenter
    fun providePresenter() = TraderMeProfitPresenter().apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trader_me_profit, container, false)

    override fun onResume() {
        super.onResume()
        presenter.onViewResumed()
    }

    override fun init() {
        initBarChart()
        initListeners()
        initPopupMenu()
    }

    private fun initBarChart() {
        with(bar_chart_trader_me_profit) {
            data = presenter.setupBarChart()
            legend.isEnabled = false
            data.setDrawValues(false)
            animateY(3000)
            setDescription("")
            axisLeft.setDrawGridLines(false)
            axisLeft.setDrawLabels(false)
            axisLeft.setDrawZeroLine(true)
            xAxis.isEnabled = false
            axisRight.isEnabled = false
        }
    }

    private fun initListeners() {
        tv_attached_post_header.setOnClickListener {
            presenter.openCreatePostScreen(true, null)
        }

        btn_attached_post_show.setOnClickListener {
            tv_attached_post_text.maxLines = 0
        }
    }

    private fun initPopupMenu() {
        val popupMenu =
            context?.let { androidx.appcompat.widget.PopupMenu(it, iv_attached_post_kebab) }
        popupMenu?.inflate(R.menu.pinned_text_menu)
        popupMenu?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.pinned_text_edit -> {
                    presenter.openCreatePostScreen(null, tv_attached_post_text.text.toString())
                    true
                }
                R.id.pinned_text_delete -> {
//                    tv_attached_post_text.text = ""
                    presenter.deletePinnedText()
                    layout_attached_post_body.visibility = View.GONE
                    tv_attached_post_header.visibility = View.VISIBLE
                    true
                }
                else -> false
            }
        }
        iv_attached_post_kebab.setOnClickListener {
            popupMenu?.show()
        }
    }

    override fun setDateJoined(date: String) {
        tv_trader_me_profit_registration_date_value.text = date
    }

    override fun setFollowersCount(followersCount: Int) {
        tv_trader_me_profit_follower_counter.text = followersCount.toString()
    }

    override fun setTradesCount(tradesCount: Int) {
        tv_trader_me_profit_deal_for_week_count.text = tradesCount.toString()
    }

    override fun setPinnedPostText(text: String?) {
        text?.let {
            layout_attached_post_body.visibility = View.VISIBLE
            tv_attached_post_text.text = text
            tv_attached_post_header.visibility = View.GONE
        }
    }
}