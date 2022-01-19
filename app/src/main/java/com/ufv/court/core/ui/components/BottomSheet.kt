package com.ufv.court.core.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ufv.court.R
import com.ufv.court.app.theme.BlackRock
import com.ufv.court.app.theme.ShipCove

@Composable
fun CustomBottomSheetContent(
    onHideBottomSheet: () -> Unit,
    options: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 24.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.select),
                style = MaterialTheme.typography.h5,
                color = BlackRock
            )
            IconButton(onClick = { onHideBottomSheet() }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        options()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomBottomSheetOption(
    @StringRes textId: Int,
    @StringRes optionDescription: Int,
    @DrawableRes iconId: Int? = null,
    imageVector: ImageVector,
    onClick: () -> Unit
) {
    Card(shape = RoundedCornerShape(16.dp), onClick = onClick, elevation = 0.dp) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (iconId == null) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = stringResource(id = optionDescription)
                )
            } else {
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = stringResource(id = optionDescription)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(id = textId),
                color = ShipCove,
                style = MaterialTheme.typography.body1
            )
        }
    }
}
