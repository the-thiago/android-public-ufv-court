package com.ufv.court.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.ufv.court.app.theme.Solitude

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    labelText: String = "",
    hintText: String = "",
    text: String,
    onTextChange: (String) -> Unit,
) {
    Column(modifier = modifier.testTag("TextInputCustomTextField")) {
        if (labelText.isNotEmpty()) {
            TextFieldLabel(text = labelText)
        }
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("TextFieldCustomTextField"),
            value = text,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Solitude,
                cursorColor = Color.Black,
                disabledLabelColor = Solitude,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            onValueChange = {
                onTextChange(it)
            },
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            maxLines = 1,
            keyboardOptions = keyboardOptions,
            trailingIcon = trailingIcon,
            leadingIcon = leadingIcon,
            visualTransformation = visualTransformation,
            textStyle = MaterialTheme.typography.body2,
            placeholder = {
                Text(
                    text = hintText,
                    style = MaterialTheme.typography.body2
                )
            }
        )
    }
}