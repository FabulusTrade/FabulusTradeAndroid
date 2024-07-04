package ru.fabulus.fabulustrade.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.github.terrakok.cicerone.Router
import moxy.MvpAppCompatFragment
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.entity.UserProfile
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.viewmodel.SetEmailViewModel
import javax.inject.Inject


class SetEmailFragment : MvpAppCompatFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var router: Router

    private val viewModel by viewModels<SetEmailViewModel> { viewModelFactory }

    companion object {
        fun newInstance() = SetEmailFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as App).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                SetEmailScreen(viewModel, router)
            }
        }
    }
}

@Composable
fun SetEmailScreen(viewModel: SetEmailViewModel, router: Router) {
    val email by viewModel.email

    val previousEmail by viewModel.previousEmail

    val successMessage by viewModel.successMessage.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    ProfileEditingDialogs(successMessage = successMessage,
        errorMessage = errorMessage,
        { router.exit() },
        { message -> viewModel.setErrorMessage(message) })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            previousEmail,
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        GreyTitle(title = stringResource(R.string.set_email_title))

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { viewModel.updateEmail(it) },
            label = { Text(stringResource(R.string.edit_email_prompt)) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        SaveButton { viewModel.saveEmail() }

        Spacer(modifier = Modifier.height(16.dp))

        BackButton { router.exit() }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultSetEmailScreenPreview() {
    // Создаем фиктивный профайл
    val fakeUserProfile = UserProfile(
        "fakeId",
        "fakeUsername",
        "fake@email.com",
        null,
        false,
        true,
        "fakeFirstName",
        "fakeLastName",
        "fakePatronmic",
        "01.01.2000",
        null,
        0,
        0
    )

    val fakeProfile = Profile(
        fakeUserProfile,
        null,
        null,
        true
    )

    // Создаем фиктивные Router и ApiRepo
    val fakeRouter = Router()

    // Инициализируем ViewModel c заданным профилем и репо
    val viewModel = SetEmailViewModel().apply {
        profile = fakeProfile
    }

    // Вызовем SetEmailScreen
    SetEmailScreen(viewModel, fakeRouter)
}