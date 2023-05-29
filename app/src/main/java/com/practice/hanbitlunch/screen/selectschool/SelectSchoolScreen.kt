package com.practice.hanbitlunch.screen.selectschool

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.practice.hanbitlunch.screen.LightAndDarkPreview
import com.practice.hanbitlunch.theme.BlindarTheme
import com.practice.hanbitlunch.theme.NanumSquareRound

@Composable
fun SelectSchoolScreen(
    onNavigateToMain: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(modifier = modifier) {
        val (text, nextButton) = createRefs()
        Text(
            text = "학교 선택 화면",
            modifier = Modifier.constrainAs(text) {
                linkTo(parent.start, parent.top, parent.end, parent.bottom)
            },
            fontFamily = NanumSquareRound,
        )
        Button(
            onClick = onNavigateToMain,
            modifier = Modifier.constrainAs(nextButton) {
                linkTo(parent.start, text.top, parent.end, parent.bottom, topMargin = 100.dp)
            }
        ) {
            Text(
                text = "메인 화면으로 이동",
                fontFamily = NanumSquareRound,
            )
        }
    }
}

@LightAndDarkPreview
@Composable
private fun SelectSchoolScreenPreview() {
    BlindarTheme {
        SelectSchoolScreen(
            onNavigateToMain = { },
            modifier = Modifier.fillMaxSize(),
        )
    }
}