package ru.fabulus.fabulustrade.ui.fragment

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.databinding.FragmentTradeDetailBinding
import ru.fabulus.fabulustrade.mvp.model.entity.Trade
import ru.fabulus.fabulustrade.mvp.presenter.CreatePostPresenter
import ru.fabulus.fabulustrade.mvp.presenter.TradeDetailPresenter
import ru.fabulus.fabulustrade.mvp.view.TradeDetailView
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.adapter.ImageListOfPostAdapter
import ru.fabulus.fabulustrade.util.showLongToast

class TradeDetailFragment : MvpAppCompatFragment(), TradeDetailView {

    enum class Mode {
        TRADER_NO_ARGUMENT,
        TRADER_FILLING_ARGUMENT,
        NOT_TRADER_NO_ARGUMENT
    }

    enum class TradeType {
        OPENING_TRADE,
        CLOSING_TRADE
    }

    private var currentMode: Mode = Mode.NOT_TRADER_NO_ARGUMENT

    private var tradeType: TradeType = TradeType.OPENING_TRADE

    private var _binding: FragmentTradeDetailBinding? = null
    private val binding: FragmentTradeDetailBinding
        get() = checkNotNull(_binding) { getString(R.string.binding_error) }

    private val imageListOfPostAdapter by lazy {
        ImageListOfPostAdapter(presenter)
    }

    lateinit var currentTrade: Trade

    companion object {
        const val TRADE_KEY = "trade"
        fun newInstance(trade: Trade) = TradeDetailFragment().apply {
            currentTrade = trade
            arguments = Bundle().apply {
                putParcelable(TRADE_KEY, trade)
            }
        }
    }

    @InjectPresenter
    lateinit var presenter: TradeDetailPresenter

    @ProvidePresenter
    fun providePresenter() = TradeDetailPresenter(requireArguments()[TRADE_KEY] as Trade).apply {
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTradeDetailBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun init() {
        binding.ivTradeDetailClose.setOnClickListener {
            presenter.closeClicked()
        }
        initClickListeners()
    }

    private fun initTextChangeListeners() {
        val price = binding.tvTradeDetailPrice.text.trim().toString()
        binding.etTakeProfit.addTextChangedListener { editText ->
            binding.tvTakeProfitResult.text = formatOnFormulaToTable(
                editText.toString(),
                price
            )
        }
        binding.etStopLoss.addTextChangedListener { editText ->
            binding.tvStopLossResult.text = formatOnFormulaToTable(
                editText.toString(),
                price
            )
        }
    }

    private fun formatOnFormulaToTable(numberText: String, priceText: String): String {
        return try {
            val number = "${numberText.replace(",", ".")}".trim().toDouble()
            val price = "${priceText.replace(",", ".")}".trim().toDouble()
            val returnValue = ((number / price) - 1) * 100
            "%.2f %%".format(returnValue)
        } catch (e: NumberFormatException) {
            ""
        }
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            presenter.addImages(uris.map { it.toBitmap() })
        }

    private fun Uri.toBitmap() =
        BitmapFactory.decodeStream(requireContext().contentResolver.openInputStream(this))

    private fun initClickListeners() {
        binding.linearShareArgumentsBegin.setOnClickListener {
            setMode(Mode.TRADER_FILLING_ARGUMENT)
        }
        binding.appCompatPublish.setOnClickListener {
            val text = binding.etCreatePost.text.toString()
            val stopLoss = binding.etStopLoss.text.toString().toFloatOrNull()
            val takeProfit = binding.etTakeProfit.text.toString().toFloatOrNull()
            val dealTerm = binding.etHowManyDays.text.toString().toIntOrNull()
            presenter.onPublishClicked(text, stopLoss, takeProfit, dealTerm)
        }
        binding.ivAttachImages.setOnClickListener {
            getContent.launch(getString(R.string.gallery_mask))
        }
    }

    override fun setMode(mode: Mode) {
        when (mode) {
            Mode.TRADER_NO_ARGUMENT -> setTraderNoArgumentMode()
            Mode.TRADER_FILLING_ARGUMENT -> setTraderFillingArgumentMode()
            Mode.NOT_TRADER_NO_ARGUMENT -> setNotTraderNoArgumentMode()
        }
        currentMode = mode
    }

    override fun setTradeType(type: TradeType) {
        when (type) {
            TradeType.OPENING_TRADE -> {
                binding.tvShareHead.text =
                    resources.getString(R.string.share_with_arguments_on_your_idea)
                binding.tvShareArgument.text =
                    resources.getString(R.string.share_with_arguments_on_your_idea)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.tvFirstLineLabel.text = Html.fromHtml(resources.getString(R.string.take_profit_price), Html.FROM_HTML_MODE_LEGACY)
                    binding.tvSecondLineLabel.text = Html.fromHtml(resources.getString(R.string.stop_loss_price), Html.FROM_HTML_MODE_LEGACY)
                } else {
                    binding.tvFirstLineLabel.text = Html.fromHtml(resources.getString(R.string.take_profit_price))
                    binding.tvSecondLineLabel.text = Html.fromHtml(resources.getString(R.string.stop_loss_price))
                }
            }
            TradeType.CLOSING_TRADE -> {
                binding.tvShareHead.text = resources.getString(R.string.arguments_head_idea)
                binding.tvShareArgument.text = resources.getString(R.string.comment_to_result)
                binding.tvFirstLineLabel.text = resources.getString(R.string.income)
                binding.tvSecondLineLabel.text = resources.getString(R.string.loss)
                binding.etTakeProfit.apply {
                    isEnabled = false
                    background = null
                }
                binding.etStopLoss.apply {
                    isEnabled = false
                    background = null
                }
                binding.etHowManyDays.apply {
                    isEnabled = false
                    background = null
                }
            }
        }
        tradeType = type
    }

    private fun setTraderNoArgumentMode() {
        binding.linearShareArgumentsBegin.visibility = View.VISIBLE
        binding.layoutArgumentTable.visibility = View.GONE
        binding.layoutPostText.visibility = View.GONE
        binding.layoutSharingPanel.visibility = View.GONE
        binding.tvShareHead.visibility = View.VISIBLE
        binding.ivTradeDetailClose.apply {
            visibility = View.INVISIBLE
            isClickable = false
        }
    }

    private fun setTraderFillingArgumentMode() {
        binding.linearShareArgumentsBegin.visibility = View.GONE
        binding.layoutArgumentTable.visibility = View.VISIBLE
        binding.layoutPostText.visibility = View.VISIBLE
        binding.layoutSharingPanel.visibility = View.VISIBLE
        binding.linearShareHead.visibility = View.VISIBLE
        binding.ivTradeDetailClose.apply {
            visibility = View.INVISIBLE
            isClickable = false
        }
    }

    private fun setNotTraderNoArgumentMode() {
        binding.tvShareHead.visibility = View.INVISIBLE
        binding.linearShareArgumentsBegin.visibility = View.GONE
        binding.layoutArgumentTable.visibility = View.GONE
        binding.layoutPostText.visibility = View.GONE
        binding.layoutSharingPanel.visibility = View.GONE
    }

    override fun setName(traderName: String) {
        binding.tvTradeDetailTraderName.text = traderName
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
        initTextChangeListeners()
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

    override fun setTakeProfit(takeProfit: Float) {
        binding.etTakeProfit.setText(takeProfit.toString())
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
    }

    override fun setLoss(loss: Float, precision: Int) {
        binding.tvStopLossResult.setText(formPercentString(loss, precision))
    }

    fun formPercentString(num: Float, precision: Int): String {
        val percent = num * 100
        return "%.${precision}f %%".format(percent)
    }

    override fun setDealTerm(term: Float, precision: Int) {
        binding.etHowManyDays.setText("%.${precision}f".format(term))
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun showImagesAddedMessage(count: Int) {
        requireContext().showLongToast(getString(R.string.images_added, count))
    }

    override fun updateListOfImages(
        images: List<CreatePostPresenter.ImageOfPost>,
        deletedImage: CreatePostPresenter.ImageOfPost?
    ) {
        imageListOfPostAdapter.updateImages(images, deletedImage)
    }

    override fun showErrorMessage(message: String) {
        context?.let {
            AlertDialog.Builder(it)
                .setTitle("ОШИБКА")
                .setMessage(message)
                .setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }
}