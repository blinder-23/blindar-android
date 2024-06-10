package com.practice.designsystem.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/**
 * Composable의 아랫변에만 선을 그린다.
 *
 * @param color 선의 색상
 * @param width 선의 두께
 */
fun Modifier.drawBottomLine(
    color: Color,
    width: Dp = Dp.Hairline,
) = this.drawBehind {
    drawLine(
        color = color,
        start = Offset(0f, size.height),
        end = Offset(size.width, size.height),
        strokeWidth = width.toPx(),
    )
}