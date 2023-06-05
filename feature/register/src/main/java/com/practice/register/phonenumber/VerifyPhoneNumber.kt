package com.practice.register.phonenumber

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.practice.designsystem.LightPreview
import com.practice.designsystem.theme.BlindarTheme
import com.practice.designsystem.theme.NanumSquareRound

@Composable
fun VerifyPhoneNumber(
    onNextButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(modifier = modifier) {
        val (text, button) = createRefs()
        Text(
            text = "폰 번호",
            modifier = Modifier.constrainAs(text) {
                linkTo(
                    top = parent.top,
                    bottom = parent.bottom,
                    start = parent.start,
                    end = parent.end,
                )
            },
            fontFamily = NanumSquareRound,
        )
        Button(
            onClick = onNextButtonClick,
            modifier = Modifier.constrainAs(button) {
                top.linkTo(text.bottom, margin = 100.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) {
            Text(text = "다음")
        }
    }
}

@LightPreview
@Composable
private fun VerifyPhoneNumberPreview() {
    BlindarTheme {
        VerifyPhoneNumber(
            onNextButtonClick = {},
        )
    }
}