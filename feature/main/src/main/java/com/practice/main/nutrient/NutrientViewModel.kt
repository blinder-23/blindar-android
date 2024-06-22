package com.practice.main.nutrient

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.practice.domain.meal.Meal
import com.practice.main.state.toUiNutrient
import com.practice.meal.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NutrientViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mealRepository: MealRepository,
) : ViewModel() {
    private val route = savedStateHandle.toRoute<NutrientRoute>()

    private val _uiState =
        MutableStateFlow<NutrientUiState>(NutrientUiState.Loading(route.date, route.mealTime))
    val uiState = _uiState.asStateFlow()

    init {
        loadNutrients()
    }

    private fun loadNutrients() {
        viewModelScope.launch {
            _uiState.value =
                mealRepository.getMeal(route.schoolCode, route.year, route.month, route.dayOfMonth)
                    .parseMealToUiState()
        }
    }

    private fun List<Meal>.parseMealToUiState(): NutrientUiState {
        val meal = this.firstOrNull { it.mealTime == route.mealTime }
        return if (meal != null) {
            NutrientUiState.Success(
                date = route.date,
                mealTime = route.mealTime,
                nutrients = meal.nutrients.toUiNutrient(meal.calorie),
            )
        } else {
            NutrientUiState.Fail(route.date, route.mealTime)
        }
    }
}