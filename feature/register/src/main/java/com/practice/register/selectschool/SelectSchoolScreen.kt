package com.practice.register.selectschool

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.practice.designsystem.LightAndDarkPreview
import com.practice.designsystem.LightPreview
import com.practice.designsystem.components.BlindarTopAppBar
import com.practice.designsystem.components.BottomNextButton
import com.practice.designsystem.theme.BlindarTheme
import com.practice.register.R
import kotlinx.collections.immutable.ImmutableList

@Composable
fun SelectSchoolScreen(modifier: Modifier = Modifier) {
    // TODO: stateful (including viewmodel etc.)
    // TODO: call stateless version here
}

@Composable
private fun SelectSchoolScreen(
    schools: ImmutableList<School>,
    onSchoolClick: (School) -> Unit,
    query: String,
    onQueryChange: (String) -> Unit,
    onBackButtonClick: () -> Unit,
    onNavigateToMain: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val horizontalMargin = 8.dp
    Column(modifier = modifier) {
        BlindarTopAppBar(
            title = stringResource(id = R.string.select_school_screen),
            onBackButtonClick = onBackButtonClick,
        )
        SearchSchoolTextField(
            query = query,
            onQueryChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 21.dp, start = horizontalMargin, end = horizontalMargin),
        )
        SchoolList(
            schools = schools,
            onSchoolClick = onSchoolClick,
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colorScheme.surface),
        )
        BottomNextButton(
            text = stringResource(R.string.next_button),
            enabled = true,
            onClick = onNavigateToMain,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 26.dp),
        )
    }
}

@Composable
private fun SearchSchoolTextField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
            )
        },
        trailingIcon = {
            IconButton(onClick = { onQueryChange("") }) {
                Icon(
                    imageVector = Icons.Outlined.Cancel,
                    contentDescription = null,
                )
            }
        }
    )
}

@LightPreview
@Composable
private fun SearchSchoolTextFieldPreview() {
    var query by remember { mutableStateOf("") }
    BlindarTheme {
        SearchSchoolTextField(
            query = query,
            onQueryChange = { query = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun SelectSchoolScreenPreview() {
    var query by remember { mutableStateOf("") }
    BlindarTheme {
        SelectSchoolScreen(
            schools = exampleSchools,
            onSchoolClick = {},
            query = query,
            onQueryChange = { query = it },
            onNavigateToMain = { },
            onBackButtonClick = {},
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
        )
    }
}