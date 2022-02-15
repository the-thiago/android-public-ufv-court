package com.ufv.court.core.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ufv.court.R
import com.ufv.court.app.theme.Solitude

@Composable
fun SingleChoiceDropDown(
    modifier: Modifier = Modifier,
    label: String = "",
    items: List<String>,
    selectedItem: String,
    isExpanded: Boolean,
    changeIsExpanded: () -> Unit,
    onItemClick: (String) -> Unit
) {
    val backgroundColor = Solitude
    Column(modifier = modifier) {
        SingleChoiceDropDownHeader(
            label,
            backgroundColor,
            changeIsExpanded,
            selectedItem,
            isExpanded
        )
        Spacer(modifier = Modifier.height(8.dp))
        SingleChoiceDropDownList(
            backgroundColor,
            isExpanded,
            items,
            changeIsExpanded,
            onItemClick,
            selectedItem
        )
    }
}

@Composable
private fun SingleChoiceDropDownHeader(
    label: String,
    backgroundColor: Color,
    changeIsExpanded: () -> Unit,
    selectedItem: String,
    isExpanded: Boolean
) {
    val arrowIconRotation by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f)
    if (label.isNotEmpty()) {
        TextFieldLabel(text = label)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .clickable { changeIsExpanded() }
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = selectedItem.ifEmpty { stringResource(R.string.select) },
            style = MaterialTheme.typography.body2
        )
        IconButton(
            modifier = Modifier
                .padding(end = 8.dp)
                .rotate(arrowIconRotation),
            onClick = { changeIsExpanded() }
        ) {
            Icon(
                imageVector = Icons.Default.ExpandMore,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun SingleChoiceDropDownList(
    backgroundColor: Color,
    isExpanded: Boolean,
    items: List<String>,
    changeIsExpanded: () -> Unit,
    onItemClick: (String) -> Unit,
    selectedItem: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
            .animateContentSize()
    ) {
        if (isExpanded) {
            items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            changeIsExpanded()
                            onItemClick(item)
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(
                        selected = selectedItem == item,
                        colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colors.primary),
                        onClick = {
                            changeIsExpanded()
                            onItemClick(item)
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = item, style = MaterialTheme.typography.body2)
                }
            }
        }
    }
}
