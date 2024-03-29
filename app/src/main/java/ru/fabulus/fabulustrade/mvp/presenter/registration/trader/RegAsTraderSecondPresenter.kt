package ru.fabulus.fabulustrade.mvp.presenter.registration.trader

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.Gender
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.entity.SignUpData
import ru.fabulus.fabulustrade.mvp.model.resource.ResourceProvider
import ru.fabulus.fabulustrade.mvp.view.registration.trader.RegAsTraderSecondView
import ru.fabulus.fabulustrade.navigation.Screens
import ru.fabulus.fabulustrade.util.DateValidation
import ru.fabulus.fabulustrade.util.isValidBirthday
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

const val DATE_UI_FORMAT_STRING = "%02d.%02d.%04d"
private const val DATE_PATTERN = "dd.MM.yyyy"

class RegAsTraderSecondPresenter(private var signUpData: SignUpData) :
    MvpPresenter<RegAsTraderSecondView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var profile: Profile

    @Inject
    lateinit var resourceProvider: ResourceProvider

    private var birthday: Long? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun openRegistrationFirstScreen() {
        router.backTo(Screens.registrationAsTraderFirstScreen(signUpData))
    }

    fun saveData(
        birthDay: String?,
        fistName: String?,
        lastName: String?,
        patronymic: String?,
        gender: Gender
    ) {
        if (!birthDay.isNullOrEmpty()
            || !fistName.isNullOrEmpty()
            || !lastName.isNullOrEmpty()
            || !patronymic.isNullOrEmpty()
        ) {
            if (profile.user == null) {
                signUpData = SignUpData(
                    signUpData.username,
                    signUpData.password,
                    signUpData.email,
                    signUpData.phone,
                    fistName,
                    lastName,
                    patronymic,
                    birthDay,
                    gender.char,
                    signUpData.is_trader
                )
            } else {
                signUpData = SignUpData(
                    first_name = fistName,
                    last_name = lastName,
                    patronymic = patronymic,
                    date_of_birth = birthDay,
                    gender = gender.char,
                    is_trader = signUpData.is_trader
                )
            }
            router.navigateTo(Screens.registrationAsTraderThirdScreen(signUpData))
        } else {
            viewState.showToast(resourceProvider.getStringResource(R.string.error_empty_require_field))
        }
    }

    fun checkBirthday(date: String) {
        when (isValidBirthday(date)) {
            DateValidation.INVALID -> viewState.setBirthdayError()
            DateValidation.CORRECT -> {
                birthday = SimpleDateFormat(
                    DATE_PATTERN,
                    Locale.getDefault()
                ).parse(date)?.time
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