package ru.fabulus.fabulustrade.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.fabulus.fabulustrade.R

@Composable
fun NicknameScreen() {
    var nickname by remember { mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text(
                stringResource(R.string.set_username_title),
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nickname,
            onValueChange = { nickname = it },
            label = { Text(stringResource(R.string.edit_nickname_prompt)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Сохраните значение nickname здесь
                // Snackbar.make(context, "Никнейм сохранён: ${nickname.text}", Snackbar.LENGTH_SHORT).show()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(stringResource(R.string.save_button))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Image(painter = painterResource(id = R.drawable.ic_arrow_back_black),
            contentDescription = null,

            modifier = Modifier
                .align(Alignment.Start)
                .clickable { println("Button Clicked!") })
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NicknameScreen()
}
