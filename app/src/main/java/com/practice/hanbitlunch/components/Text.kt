package com.practice.hanbitlunch.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun BigTitle(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colors.onSurface
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.h1,
        color = textColor,
    )
}

@Composable
fun Title(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colors.onSurface
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.h2,
        color = textColor,
    )
}

@Composable
fun SubTitle(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colors.onSurface,
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.h3,
        color = textColor,
    )
}

@Composable
fun Body(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colors.onSurface
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.body1,
        color = textColor,
    )
}

@Composable
fun BodySmall(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colors.onSurface
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.body2,
        color = textColor,
    )
}

@Composable
fun Caption(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colors.onSurface
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.caption,
        color = textColor,
    )
}

@Composable
fun ButtonText(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colors.onSurface
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.button,
        color = textColor,
    )
}