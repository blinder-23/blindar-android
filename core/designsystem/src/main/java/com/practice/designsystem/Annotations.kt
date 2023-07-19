package com.practice.designsystem

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Light", showBackground = true)
annotation class LightPreview

@Preview(name = "Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
annotation class DarkPreview

@LightPreview
@DarkPreview
annotation class LightAndDarkPreview

@Preview(
    name = "LightTablet",
    showBackground = true,
    device = "spec:width=1280dp,height=800dp,dpi=240"
)
annotation class LightTabletPreview