package com.practice.register.selectschool

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import com.practice.designsystem.LightAndDarkPreview
import com.practice.designsystem.components.BlindarTopAppBar
import com.practice.designsystem.components.BottomNextButton
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
        val (appBar, text, selectSchoolNextButton) = createRefs()
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
        BottomNextButton(
            text=stringResource(R.string.next_button),
            enabled = true,
            onClick = onNavigateToMain,
            modifier = Modifier
                .constrainAs(selectSchoolNextButton) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
        )
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