package com.practice.main.daily.picker

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class DateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // YYYY.MM.DD
        val trimmed = if (text.length <= 8) text else text.substring(0..7)
        val outputString = format(trimmed)
        return TransformedText(
            text = AnnotatedString(outputString),
            offsetMapping = dateOffset
        )
    }

    private fun format(trimmedText: CharSequence): String {
        val output = StringBuilder()
        trimmedText.forEachIndexed { index, c ->
            output.append(c)
            if (index == 3 || index == 5) {
                output.append(".")
            }
        }
        return output.toString()
    }

    companion object {
        private val dateOffset = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when (offset) {
                    in 0..3 -> offset
                    in 4..5 -> offset + 1
                    in 6..7 -> offset + 2
                    in 8..15 -> offset + 2 // 완충구간
                    else -> throw IndexOutOfBoundsException("original offset out of bounds: $offset")
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when (offset) {
                    in 0..3 -> offset
                    in 4..6 -> offset - 1
                    in 7..9 -> offset - 2
                    in 10..15 -> offset - 2 // 완충구간
                    else -> throw IndexOutOfBoundsException("transformed offset out of bounds: $offset")
                }
            }
        }
    }
}