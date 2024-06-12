package com.practice.register.selectschool

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.practice.designsystem.DarkPreview
import com.practice.designsystem.components.BodyLarge
import com.practice.designsystem.modifier.drawBottomBorder
import com.practice.designsystem.modifier.drawTopBorder
import com.practice.designsystem.theme.BlindarTheme
import com.practice.domain.School
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
    // TODO: 선택된 학교 보여주기
    Column(
        modifier = modifier
            .verticalScroll(scrollState)
    ) {
        schools.forEach { school ->
            SchoolItem(
                school = school,
                onClick = onSchoolClick,
            )
        }
    }
}

@Composable
private fun SchoolItem(
    school: School,
    onClick: (School) -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        headlineContent = {
            TextButton(
                onClick = { onClick(school) },
                colors = ButtonDefaults.textButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(0.dp),
            ) {
                BodyLarge(
                    text = school.name,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1f),
                )
            }
        },
        modifier = modifier
            .drawBottomBorder(MaterialTheme.colorScheme.surfaceVariant, 2.dp)
            .drawTopBorder(MaterialTheme.colorScheme.surfaceVariant, 2.dp),
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surface,
            headlineColor = MaterialTheme.colorScheme.onSurface,
        )
    )
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
    BlindarTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = 8.dp),
        ) {
            SchoolItem(
                school = exampleSchool,
                onClick = {},
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