package ru.wintrade.mvp.presenter.registration.trader

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.wintrade.mvp.model.entity.RegistrationTraderData
import ru.wintrade.mvp.model.entity.TraderRegistrationInfo
import ru.wintrade.mvp.view.registration.trader.RegAsTraderSecondView
import ru.wintrade.navigation.Screens
import ru.wintrade.util.DateValidation
import ru.wintrade.util.isValidBirthday
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

const val DATE_UI_FORMAT_STRING = "%02d.%02d.%04d"
const val DATE_PATTERN = "dd.MM.yyyy"

class RegAsTraderSecondPresenter(private val registrationData: RegistrationTraderData) : MvpPresenter<RegAsTraderSecondView>() {
    @Inject
    lateinit var router: Router

    private var birthday: Long? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun openRegistrationFirstScreen() {
        router.navigateTo(Screens.registrationAsTraderFirstScreen(registrationData))
    }

    fun openRegistrationThirdScreen(traderInfo: TraderRegistrationInfo) {
        router.navigateTo(Screens.registrationAsTraderThirdScreen(traderInfo))
    }

    fun checkBirthday(date: String) {
        when (isValidBirthday(date)) {
            DateValidation.INVALID -> viewState.setBirthdayError()
            DateValidation.CORRECT -> {
                birthday = SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).parse(date)?.time

            }
        }
    }

    fun launchBirthdayDataPicker() {
        viewState.showBirthdayDataPicker(birthday ?: Date().time)
    }

    fun setNewBirthday(newDate: Long) {
        Calendar.getInstance().apply {
            time = Date(newDate)
            val stringDate = String.format(
                DATE_UI_FORMAT_STRING,
                this[Calendar.DAY_OF_MONTH],
                this[Calendar.MONTH].plus(1),
                this[Calendar.YEAR]
            )
            if (isValidBirthday(stringDate) == DateValidation.CORRECT)
                viewState.setTraderBirthday(stringDate)
            else
                viewState.setBirthdayError()
        }
    }

}