package com.practice.register.phonenumber

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.practice.designsystem.LightPreview
import com.practice.designsystem.components.BlindarTopAppBar
import com.practice.designsystem.components.BottomNextButton
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
        val (appBar, text, phoneNextButton) = createRefs()
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
                top.linkTo(appBar.bottom, margin = 100.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            fontFamily = NanumSquareRound,
        )
        BottomNextButton(
            text = stringResource(R.string.next_button),
            enabled = true,
            onClick = onNextButtonClick,
            modifier = Modifier
                .constrainAs(phoneNextButton) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
        )
    }
}

@LightPreview
@Composable
private fun VerifyPhoneNumberPreview() {
    BlindarTheme {
        VerifyPhoneNumber(
            onBackButtonClick = {},
            onNextButtonClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(MaterialTheme.colorScheme.background),
        )
    }
}