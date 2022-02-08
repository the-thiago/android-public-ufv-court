package com.ufv.court.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
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
    maxLines: Int = 1,
    enabled: Boolean = true,
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
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                disabledTextColor = LocalContentColor.current.copy(LocalContentAlpha.current)
            ),
            onValueChange = {
                onTextChange(it)
            },
            shape = RoundedCornerShape(8.dp),
            singleLine = maxLines == 1,
            maxLines = maxLines,
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
            },
            enabled = enabled
        )
    }
}
