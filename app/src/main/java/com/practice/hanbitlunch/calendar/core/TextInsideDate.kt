package com.practice.hanbitlunch.calendar.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun TextsInsideDate(
    texts: List<String>,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(4.dp),
    color: Color = MaterialTheme.colors.onBackground,
    style: TextStyle = MaterialTheme.typography.caption,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Ellipsis,
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement
    ) {
        texts.forEach { text ->
            TextInsideDate(
                text = text,
                color = color,
                style = style,
                maxLines = maxLines,
                overflow = overflow,
            )
        }
    }
}

@Composable
fun TextInsideDate(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onBackground,
    style: TextStyle = MaterialTheme.typography.caption,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Ellipsis,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = style,
        maxLines = maxLines,
        overflow = overflow,
    )
}