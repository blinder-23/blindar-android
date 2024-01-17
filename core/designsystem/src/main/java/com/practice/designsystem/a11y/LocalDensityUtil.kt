package com.practice.designsystem.a11y

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Density

/***
 * 기기의 폰트 크기가 새로운 UI가 필요할 정도로 큰 지 판별한다.
 * 이 값이 `true`라면 큰 글씨에도 대응하는 UI를 보여줄 필요가 있다.
 */
val Density.isLargeFont: Boolean
    @Composable get() = this.fontScale >= 1.6