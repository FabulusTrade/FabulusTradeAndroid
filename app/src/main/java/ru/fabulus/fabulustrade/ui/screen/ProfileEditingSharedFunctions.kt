package ru.fabulus.fabulustrade.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.fabulus.fabulustrade.R

@Composable
fun GreyTitle(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Text(
            title,
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .padding(8.dp)
        )
    }
}

@Composable
fun SaveButton(onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = {
                onClick()
            },
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(stringResource(R.string.save_button))
        }
    }
}

@Composable
fun BackButton(onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Image(painter = painterResource(id = R.drawable.ic_arrow_back_black),
            contentDescription = null,

            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable { onClick() })
    }
}


@Composable
fun AlertDialog(
    onDismissRequest: () -> Unit,
    dialogTitle: String,
    dialogText: String,
) {
    androidx.compose.material.AlertDialog(
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("OK")
            }
        },
    )
}

@Composable
fun ProfileEditingDialogs(
    successMessage: String?,
    errorMessage: String?,
    onExit: () -> Unit,
    setErrorMessage: (String?) -> Unit
) {
    successMessage?.let {
        AlertDialog(
            onDismissRequest = { onExit() },
            dialogTitle = "Результат операции",
            dialogText = it
        )
    }

    var showDialog by remember { mutableStateOf(false) }

    errorMessage?.let {
        showDialog = true
    }

    if (showDialog && errorMessage != null) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                setErrorMessage(null)
            },
            dialogTitle = "Результат операции",
            dialogText = errorMessage
        )
    }
}
