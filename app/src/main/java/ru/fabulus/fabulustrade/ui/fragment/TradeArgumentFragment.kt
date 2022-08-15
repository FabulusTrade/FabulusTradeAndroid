package ru.fabulus.fabulustrade.ui.fragment

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.FragmentTradeArgumentBinding
import ru.fabulus.fabulustrade.mvp.model.entity.Argument
import ru.fabulus.fabulustrade.mvp.model.entity.Complaint
import ru.fabulus.fabulustrade.mvp.model.entity.Trade
import ru.fabulus.fabulustrade.mvp.presenter.BasePostPresenter
import ru.fabulus.fabulustrade.mvp.presenter.TradeArgumentPresenter
import ru.fabulus.fabulustrade.mvp.view.BasePostView
import ru.fabulus.fabulustrade.mvp.view.TradeArgumentView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.adapter.CommentRVAdapter


class TradeArgumentFragment : BasePostFragment(), TradeArgumentView {

    companion object {
        const val ARGUMENT_KEY = "argument"
        const val TRADE_KEY = "trade"
        fun newInstance(trade: Trade, argument: Argument) = TradeArgumentFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARGUMENT_KEY, argument)
                putParcelable(TRADE_KEY, trade)
            }
        }

        fun newInstance(trade: Trade) = TradeArgumentFragment().apply {
            arguments = Bundle().apply {
                putParcelable(TRADE_KEY, trade)
            }
        }
    }

    private var _binding: FragmentTradeArgumentBinding? = null

    private val binding: FragmentTradeArgumentBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    @InjectPresenter
    lateinit var tradeArgumentPresenter: TradeArgumentPresenter

    @ProvidePresenter
    fun provideTradeArgumentPresenter() =
        TradeArgumentPresenter(requireArguments()[TradeDetailFragment.TRADE_KEY] as Trade).apply {
            App.instance.appComponent.inject(this)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentTradeArgumentBinding.inflate(inflater, container, false)
        App.instance.appComponent.inject(this)
        presenter = tradeArgumentPresenter as BasePostPresenter<BasePostView>
        postBinding = checkNotNull(_binding) { getString(R.string.binding_error) }.incPostLayout
        return _binding?.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun initRecyclerView() {
        commentRVAdapter = CommentRVAdapter(tradeArgumentPresenter.listPresenter)
        postBinding.rvPostComments.run {
            adapter = commentRVAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun setType(type: String) {
        binding.tvTradeDetailOperation.text = type
    }

    override fun setCompany(company: String) {
        binding.tvTradeDetailCompany.text = company
    }

    override fun setTicker(ticker: String) {
        binding.tvTradeDetailTicker.text = ticker
    }

    override fun setPrice(price: String) {
        binding.tvTradeDetailPrice.text = price
    }

    override fun setPriceTitle(priceTitle: String) {
        binding.tvTradeDetailPriceTitle.text = priceTitle
    }

    override fun setDate(date: String) {
        binding.tvTradeDetailDate.text = date
    }

    override fun setSubtype(type: String) {
        binding.tvTradeDetailType.text = type
    }

    override fun setRepostCount(text: String) {
        postBinding.incItemPostFooter.tvRepostCount.text = text
    }

    override fun setTradeType(type: TradeDetailFragment.TradeType) {
        when (type) {
            TradeDetailFragment.TradeType.OPENING_TRADE -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.tvFirstLineLabel.text = Html.fromHtml(resources.getString(R.string.take_profit_price), Html.FROM_HTML_MODE_LEGACY)
                    binding.tvSecondLineLabel.text = Html.fromHtml(resources.getString(R.string.stop_loss_price), Html.FROM_HTML_MODE_LEGACY)
                } else {
                    binding.tvFirstLineLabel.text = Html.fromHtml(resources.getString(R.string.take_profit_price))
                    binding.tvSecondLineLabel.text = Html.fromHtml(resources.getString(R.string.stop_loss_price))
                }
            }
            TradeDetailFragment.TradeType.CLOSING_TRADE -> {
                binding.tvShareHead.text = resources.getString(R.string.arguments_head_idea)
                binding.tvFirstLineLabel.text = resources.getString(R.string.income)
                binding.tvSecondLineLabel.text = resources.getString(R.string.loss)
            }
        }
    }

    override fun setTakeProfit(takeProfit: Float) {
        binding.etTakeProfit.setText(takeProfit.toString())
        val priceText = binding.tvTradeDetailPrice.text.trim().toString()
        val price = "${priceText.replace(",", ".")}".trim().toDouble()
        binding.tvTakeProfitResult.text = "%.2f %%".format(((takeProfit / price) - 1) * 100)
    }

    override fun setProfit(profit: Float, precision: Int) {
        binding.etTakeProfit.setText(formPercentString(profit, precision))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.etTakeProfit.setTextColor(
                resources.getColor(
                    R.color.colorGreenPercentProfit, requireContext().theme
                )
            )
        } else {
            binding.etTakeProfit.setTextColor(resources.getColor(R.color.colorGreenPercentProfit))
        }
    }

    override fun setStopLoss(stopLoss: Float) {
        binding.etStopLoss.setText(stopLoss.toString())
        val priceText = binding.tvTradeDetailPrice.text.trim().toString()
        val price = "${priceText.replace(",", ".")}".trim().toDouble()
        binding.tvStopLossResult.text = "%.2f %%".format(((stopLoss / price) - 1) * 100)
    }

    override fun setLoss(loss: Float, precision: Int) {
        binding.tvStopLossResult.setText(formPercentString(loss, precision))
    }

    override fun setDealTerm(term: Double, precision: Int) {
        binding.etHowManyDays.setText("%.${precision}f".format(term))
    }

    override fun setDealTerm(term:  Int) {
        binding.etHowManyDays.setText(term.toString())
    }

    fun formPercentString(num: Float, precision: Int): String {
        val percent = num * 100
        return "%.${precision}f %%".format(percent)
    }

    override fun setPostMenuSelf(argument: Argument) {
        binding.ivAttachedKebab.setOnClickListener { btn ->
            val menu = PopupMenu(context, btn)
            menu.inflate(R.menu.menu_self_comment)

            menu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {

                    R.id.edit_comment -> {
                        tradeArgumentPresenter.editPost()
                        return@setOnMenuItemClickListener true
                    }
                    R.id.copy_comment_text -> {
                        tradeArgumentPresenter.copyPost()
                        return@setOnMenuItemClickListener true
                    }
                    R.id.delete_comment -> {
                        tradeArgumentPresenter.deletePost()
                        return@setOnMenuItemClickListener true
                    }
                    else -> return@setOnMenuItemClickListener false
                }
            }
            menu.show()
        }
    }

    override fun setPostMenuSomeone(argument: Argument, complaintList: List<Complaint>) {
        binding.ivAttachedKebab.setOnClickListener { btn ->
            val popupMenu = PopupMenu(context, btn)
            popupMenu.inflate(R.menu.menu_someone_comment)
            val complaintItem = popupMenu.menu.findItem(R.id.mi_complain_on_post)
            complaintList.forEach { complaint ->
                complaintItem.subMenu.add(Menu.NONE, complaint.id, Menu.NONE, complaint.text)
                    .setOnMenuItemClickListener {
                        tradeArgumentPresenter.complainOnPost(complaint.id)
                        return@setOnMenuItemClickListener true
                    }
            }

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.mi_copy_post_text -> {
                        tradeArgumentPresenter.copyPost()
                        return@setOnMenuItemClickListener true
                    }
                    else -> return@setOnMenuItemClickListener false
                }
            }
            popupMenu.show()
        }
    }

    override fun relocateKebabForOpeningTrade() {
        val constraintLayout: ConstraintLayout = binding.clTradeArgument
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.connect(R.id.fl_kebab_bar,
            ConstraintSet.TOP,
            R.id.linear_layout_detail,
            ConstraintSet.BOTTOM)
        constraintSet.connect(R.id.layout_argument_table,
            ConstraintSet.TOP,
            R.id.fl_kebab_bar,
            ConstraintSet.BOTTOM)
        constraintSet.connect(R.id.inc_post_layout,
            ConstraintSet.TOP,
            R.id.layout_argument_table,
            ConstraintSet.BOTTOM)
        constraintSet.applyTo(constraintLayout)
    }
}