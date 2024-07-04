package ru.fabulus.fabulustrade.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.launch
import moxy.MvpAppCompatFragment
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.entity.Profile
import ru.fabulus.fabulustrade.mvp.model.entity.UserProfile
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.viewmodel.SetPasswordSecondViewModel
import javax.inject.Inject


class SetPasswordSecondFragment : MvpAppCompatFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var router: Router

    private val viewModel by viewModels<SetPasswordSecondViewModel> { viewModelFactory }

    companion object {
        fun newInstance() = SetPasswordSecondFragment().apply {
            arguments = Bundle().apply {}
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
                SetPasswordSecondScreen(viewModel, router)
            }
        }
    }
}

@Composable
fun SetPasswordSecondScreen(viewModel: SetPasswordSecondViewModel, router: Router) {
    val code by viewModel.code
    val password by viewModel.password
    val passwordConfirmation by viewModel.passwordConfirmation

    val successMessage by viewModel.successMessage.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val showPassword = remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { contentPadding ->
        ProfileEditingDialogs(successMessage = successMessage,
            errorMessage = errorMessage,
            { router.exit() },
            { message -> viewModel.setErrorMessage(message) })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                stringResource(R.string.set_password_title),
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(top = 8.dp)
            )

            Text(
                stringResource(R.string.set_password_subtitle_second),
                textAlign = TextAlign.Center,
                color = Color.Gray,
                modifier = Modifier.padding(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = code,
                onValueChange = { viewModel.updateCode(it) },
                label = { Text(stringResource(R.string.edit_email_second_code_prompt)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))


            OutlinedTextField(label = { Text(stringResource(R.string.edit_email_second_password_prompt)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                value = password,
                onValueChange = { viewModel.updatePassword(it) },

                keyboardOptions = KeyboardOptions.Default.copy(
                    autoCorrect = true, keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                ),
                singleLine = true,
                visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (showPassword.value) {
                        Icons.Filled.Visibility
                    } else {
                        Icons.Filled.VisibilityOff
                    }

                    IconButton(onClick = { showPassword.value = !showPassword.value }) {
                        Icon(
                            icon,
                            contentDescription = "Visibility",
                        )
                    }
                })

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = passwordConfirmation,
                onValueChange = { viewModel.updatePasswordConfirmation(it) },
                label = { Text(stringResource(R.string.edit_email_second_password_repeat_prompt)) },
                singleLine = true,
                visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            val snackbarText =
                stringResource(R.string.edit_email_second_passwords_dont_match_snackbar_text)

            SaveButton {
                if (password == passwordConfirmation) {
                    viewModel.changePassword()

                } else {
                    scope.launch {
                        snackbarHostState.showSnackbar(snackbarText)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            BackButton { router.exit() }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultSetPasswordSecondScreenPreview() {
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
        fakeUserProfile, null, null, true
    )

    // Создаем фиктивные Router и ApiRepo
    val fakeRouter = Router()

    // Инициализируем ViewModel c заданным профилем и репо
    val viewModel = SetPasswordSecondViewModel().apply {
        profile = fakeProfile
    }

    // Вызовем SetPasswordSecondScreen
    SetPasswordSecondScreen(viewModel, fakeRouter)
}