package com.practice.designsystem

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Light", showBackground = true)
annotation class LightPreview

annotation class DarkPreview
@Preview(name = "Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)

@LightPreview
@DarkPreview
annotation class LightAndDarkPreview