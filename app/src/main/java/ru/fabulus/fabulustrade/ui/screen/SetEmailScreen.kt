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
    val nickname by viewModel.nickname

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

        GreyTitle(title = stringResource(R.string.set_email_title))

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nickname,
            onValueChange = { viewModel.updateNickname(it) },
            label = { Text(stringResource(R.string.edit_email_prompt)) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        SaveButton { viewModel.saveNickname() }

        Spacer(modifier = Modifier.height(16.dp))

        BackButton { router.exit() }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultSetEmailScreenPreview() {
    SetEmailScreen(SetEmailViewModel(), Router())
}