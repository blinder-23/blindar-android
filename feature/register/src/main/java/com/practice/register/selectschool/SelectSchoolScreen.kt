package com.practice.register.selectschool

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.practice.designsystem.LightAndDarkPreview
import com.practice.designsystem.LightPreview
import com.practice.designsystem.components.BlindarTopAppBar
import com.practice.designsystem.components.TitleSmall
import com.practice.designsystem.theme.BlindarTheme
import com.practice.domain.School
import com.practice.register.R
import com.practice.register.RegisterViewModel
import com.practice.util.makeToast
import kotlinx.collections.immutable.ImmutableList

@Composable
fun SelectSchoolScreen(
    onBackButtonClick: () -> Unit,
    onNavigateToMain: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel(),
) {
    val uiState by viewModel.registerUiState

    val context = LocalContext.current
    val baseSuccessMessage = stringResource(id = R.string.school_selected_base)
    SelectSchoolScreen(
        selectedSchool = uiState.selectedSchool,
        schools = uiState.schools,
        onSchoolClick = { school ->
            viewModel.onSchoolClick(
                context = context,
                school = school,
            ) {
                val successMessage = "${school.name} $baseSuccessMessage"
                onNavigateToMain()
                context.makeToast(successMessage)
            }
        },
        query = uiState.schoolQuery,
        onQueryChange = viewModel::onSchoolQueryChange,
        onBackButtonClick = onBackButtonClick,
        modifier = modifier,
    )
}

@Composable
private fun SelectSchoolScreen(
    selectedSchool: School,
    schools: ImmutableList<School>,
    onSchoolClick: (School) -> Unit,
    query: String,
    onQueryChange: (String) -> Unit,
    onBackButtonClick: () -> Unit,
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
            selectedSchool = selectedSchool,
            schools = schools,
            onSchoolClick = onSchoolClick,
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colorScheme.surface),
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
        label = {
            TitleSmall(text = stringResource(R.string.school_text_field_label))
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Outlined.Cancel,
                        contentDescription = stringResource(R.string.school_clear_text_field),
                    )
                }
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
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
            selectedSchool = exampleSchools[0],
            schools = exampleSchools,
            onSchoolClick = {},
            query = query,
            onQueryChange = { query = it },
            onBackButtonClick = {},
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
        )
    }
}