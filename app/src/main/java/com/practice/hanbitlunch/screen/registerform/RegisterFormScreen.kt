package com.practice.hanbitlunch.screen.registerform

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.practice.hanbitlunch.screen.LightAndDarkPreview
import com.practice.hanbitlunch.theme.BlindarTheme

@Composable
fun RegisterFormScreen(
    onNextButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(modifier = modifier) {
        val (text, nextButton) = createRefs()
        Text(
            text = "회원가입 폼",
            modifier = Modifier.constrainAs(text) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
        )
        Button(
            onClick = onNextButtonClick,
            modifier = Modifier.constrainAs(nextButton) {
                top.linkTo(text.bottom, margin = 100.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) {
            Text(text = "다음")
        }
    }
}

@LightAndDarkPreview
@Composable
private fun RegisterFormScreenPreview() {
    BlindarTheme {
        RegisterFormScreen(
            onNextButtonClick = {  },
            modifier = Modifier.fillMaxSize(),
        )
    }
}