package com.practice.register.selectschool

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.practice.designsystem.LightPreview
import com.practice.designsystem.components.BodyLarge
import com.practice.designsystem.theme.BlindarTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun SchoolList(
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
        modifier = modifier,
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surface,
            headlineColor = MaterialTheme.colorScheme.onSurface,
        )
    )
}

internal val exampleSchool = School(name = "한빛맹학교", schoolId = 0)
internal val exampleSchools =
    (1..20).map {
        School(
            name = "${exampleSchool.name} $it",
            schoolId = it
        )
    }.toImmutableList()

@LightPreview
@Composable
private fun SchoolItemPreview() {
    BlindarTheme {
        SchoolItem(
            school = exampleSchool,
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@LightPreview
@Composable
private fun SchoolListPreview() {
    BlindarTheme {
        SchoolList(
            schools = exampleSchools,
            onSchoolClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp),
        )
    }
}