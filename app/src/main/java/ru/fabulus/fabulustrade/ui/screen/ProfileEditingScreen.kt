package ru.fabulus.fabulustrade.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.github.terrakok.cicerone.Router
import moxy.MvpAppCompatFragment
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.viewmodel.ProfileEditingViewModel
import javax.inject.Inject

class ProfileEditingFragment : MvpAppCompatFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var router: Router

    private val viewModel by viewModels<ProfileEditingViewModel> { viewModelFactory }

    companion object {
        fun newInstance() = ProfileEditingFragment().apply {
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
                ProfileEditingScreen(viewModel)
            }
        }
    }
}

@Composable
fun ProfileEditingScreen(viewModel: ProfileEditingViewModel) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        val offsetYPercent = 0.1f
        val screenHeight = constraints.maxHeight
        val offsetY = (screenHeight * offsetYPercent).toInt()

        Column(
            modifier = Modifier
                .offset {
                    IntOffset(x = 0, y = offsetY)
                }
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(R.string.profile_editing_title),
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .padding(bottom = 32.dp)
            )

            Button(
                onClick = {
                    viewModel.openUsernameEditor()
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
            ) {
                Text(stringResource(R.string.profile_editing_edit_username))
            }

            Button(
                onClick = {
                    viewModel.openFirstAndLastNamesEditor()
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
            ) {
                Text(stringResource(R.string.profile_editing_edit_fullname))
            }

            Button(
                onClick = {
                    viewModel.openEmailEditor()
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
            ) {
                Text(stringResource(R.string.profile_editing_edit_email))
            }

            Button(
                onClick = {
                },
                enabled = false,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
            ) {
                Text(stringResource(R.string.profile_editing_edit_phone))
            }

            Button(
                onClick = {
                },
                enabled = false,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
            ) {
                Text(stringResource(R.string.profile_editing_edit_password))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultProfileEditingPreview() {
    ProfileEditingScreen(ProfileEditingViewModel())
}
