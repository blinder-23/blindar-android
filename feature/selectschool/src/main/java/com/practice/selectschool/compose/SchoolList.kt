package com.practice.selectschool.compose

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.unit.dp
import com.practice.designsystem.DarkPreview
import com.practice.designsystem.components.BodyLarge
import com.practice.designsystem.modifier.drawBottomBorder
import com.practice.designsystem.modifier.drawTopBorder
import com.practice.designsystem.theme.BlindarTheme
import com.practice.domain.School
import com.practice.selectschool.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun SchoolList(
    selectedSchool: School,
    schools: ImmutableList<School>,
    onSchoolClick: (School) -> Unit,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
) {
    Column(
        modifier = modifier
            .verticalScroll(scrollState)
    ) {
        schools.forEach { school ->
            SchoolItem(
                school = school,
                isSelected = school == selectedSchool,
                onClick = onSchoolClick,
            )
        }
    }
}

@Composable
private fun SchoolItem(
    school: School,
    isSelected: Boolean,
    onClick: (School) -> Unit,
    modifier: Modifier = Modifier,
) {
    val alpha = if (isSelected) 1f else 0f
    val label = stringResource(
        id = if (isSelected) R.string.school_item_selected else R.string.school_item_unselected,
        school.name
    )

    Row(
        modifier = modifier
            .clickable { onClick(school) }
            .background(MaterialTheme.colorScheme.surface)
            .drawBottomBorder(MaterialTheme.colorScheme.surfaceVariant, 2.dp)
            .drawTopBorder(MaterialTheme.colorScheme.surfaceVariant, 2.dp)
            .padding(16.dp)
            .clearAndSetSemantics {
                role = Role.Button
                contentDescription = label
            },
    ) {
        BodyLarge(
            text = school.name,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Outlined.Check,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.graphicsLayer {
                this.alpha = alpha
            },
        )
    }
}

internal val exampleSchool = School(name = "한빛맹학교", schoolCode = 0)
internal val exampleSchools =
    (1..20).map {
        School(
            name = "${exampleSchool.name} $it",
            schoolCode = it
        )
    }.toImmutableList()

@DarkPreview
@Composable
private fun SchoolItemPreview() {
    var isSelected by remember { mutableStateOf(false) }
    BlindarTheme {
        Box(modifier = Modifier.padding(vertical = 8.dp)) {
            SchoolItem(
                school = exampleSchool,
                isSelected = isSelected,
                onClick = { isSelected = !isSelected },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@DarkPreview
@Composable
private fun SchoolListPreview() {
    BlindarTheme {
        SchoolList(
            selectedSchool = exampleSchools[0],
            schools = exampleSchools,
            onSchoolClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp),
        )
    }
}