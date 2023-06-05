package com.practice.register.phonenumber

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.practice.designsystem.LightPreview
import com.practice.designsystem.components.BlindarTopAppBar
import com.practice.designsystem.theme.BlindarTheme
import com.practice.designsystem.theme.NanumSquareRound
import com.practice.register.R

@Composable
fun VerifyPhoneNumber(
    onBackButtonClick: () -> Unit,
    onNextButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(modifier = modifier) {
        val (appBar, text, button) = createRefs()
        BlindarTopAppBar(
            title = stringResource(id = R.string.verify_phone_screen),
            onBackButtonClick = onBackButtonClick,
            modifier = Modifier.constrainAs(appBar) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
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
            onBackButtonClick = {},
            onNextButtonClick = {},
        )
    }
}