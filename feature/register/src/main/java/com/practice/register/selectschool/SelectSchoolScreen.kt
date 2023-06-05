package com.practice.register.selectschool

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.practice.designsystem.LightAndDarkPreview
import com.practice.designsystem.components.BlindarTopAppBar
import com.practice.designsystem.theme.BlindarTheme
import com.practice.designsystem.theme.NanumSquareRound
import com.practice.register.R

@Composable
fun SelectSchoolScreen(
    onBackButtonClick: () -> Unit,
    onNavigateToMain: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(modifier = modifier) {
        val (appBar, text, nextButton) = createRefs()
        BlindarTopAppBar(
            title = stringResource(id = R.string.select_school_screen),
            onBackButtonClick = onBackButtonClick,
            modifier = Modifier.constrainAs(appBar) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
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
            onBackButtonClick = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}