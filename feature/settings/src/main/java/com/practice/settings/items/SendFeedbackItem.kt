package com.practice.settings.items

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.practice.designsystem.LightAndDarkPreview
import com.practice.designsystem.components.LabelMedium
import com.practice.designsystem.components.TitleMedium
import com.practice.designsystem.theme.BlindarTheme
import com.practice.settings.R

@Composable
internal fun SendFeedbackItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick, role = Role.Button)
            .semantics(mergeDescendants = true) {},
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SendFeedbackItemText(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
        )
        SendFeedbackItemIcon()
    }
}

@Composable
private fun SendFeedbackItemText(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        TitleMedium(text = stringResource(id = R.string.settings_send_feedback_item_title))
        LabelMedium(
            text = stringResource(id = R.string.settings_send_feedback_item_body),
            modifier = Modifier.clearAndSetSemantics {
                contentDescription = ""
            },
        )
    }
}

@Composable
private fun SendFeedbackItemIcon(
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = Icons.Default.ChevronRight,
        contentDescription = null,
        tint = contentColorFor(backgroundColor = MaterialTheme.colorScheme.surface),
        modifier = modifier.minimumInteractiveComponentSize(),
    )
}

@LightAndDarkPreview
@Composable
private fun SendFeedbackItemPreview() {
    BlindarTheme {
        SendFeedbackItem(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface),
        )
    }
}