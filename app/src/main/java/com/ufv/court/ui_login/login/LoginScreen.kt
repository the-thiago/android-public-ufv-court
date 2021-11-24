package com.ufv.court.ui_login.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "UFV court")
        TextField(value = "email", onValueChange = {})
        TextField(value = "password", onValueChange = {})
    }
}
