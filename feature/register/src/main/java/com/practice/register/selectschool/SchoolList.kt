package com.practice.register.selectschool

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.practice.designsystem.LightPreview
import com.practice.designsystem.components.BodyLarge
import com.practice.designsystem.theme.BlindarTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun SchoolList(
    schools: ImmutableList<School>,
    onSchoolClick: (School) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
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
            BodyLarge(text = school.name)
        },
        modifier = modifier
            .clickable { onClick(school) },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surface,
            headlineColor = MaterialTheme.colorScheme.onSurface,
        )
    )
}

internal val exampleSchool = School(name = "한빛맹학교", schoolId = "")
internal val exampleSchools =
    (1..20).map { exampleSchool.copy(schoolId = it.toString()) }.toImmutableList()

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
            modifier = Modifier.fillMaxSize(),
        )
    }
}