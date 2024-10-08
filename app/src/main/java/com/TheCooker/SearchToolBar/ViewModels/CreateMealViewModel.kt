package com.TheCooker.SearchToolBar.ViewModels


import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TheCooker.Login.SignIn.UserDataProvider
import com.TheCooker.R
import com.TheCooker.SearchToolBar.RecipeRepo.RecipeRepo
import com.TheCooker.SearchToolBar.ApiService.UserRecipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateMealViewModel @Inject constructor(
    private val userDataProvider: UserDataProvider,
    private val recipeRepo: RecipeRepo,

): ViewModel()  {

    data class UserRecipeState(
        val error: String? = null,
        val isSaving: Boolean = false,
        val saveSuccess: Boolean = false
    )

    val creatorId: String?
        get() = userDataProvider.userData?.uid


    private val _saveState = mutableStateOf(UserRecipeState())
    val saveState: State<UserRecipeState> = _saveState

    private val _mealName = mutableStateOf("")
    val mealName: State<String> = _mealName

    private val _ingredients = mutableStateListOf<String>("", "")
    val ingredients: List<String> = _ingredients

    private val _steps = mutableStateListOf<String>("", "")
    val steps: List<String> = _steps

    //MealName
    fun onMealNameChange(newValue: String) {
        _mealName.value = newValue
    }
    //Ingredients
    fun addIngredientAfter(index: Int){
        _ingredients.add(index + 1, "")

    }

    fun addIngredientAtEnd(){
        _ingredients.add("")
    }

    fun updateIngredient(index: Int, newValue: String){
        _ingredients[index] = newValue
    }


    fun removeIngredient(index: Int){
        if(_ingredients.size > 2) {
            _ingredients.removeAt(index)
        }
    }

    //Steps

    fun addStepAfter(index: Int){
        _steps.add(index + 1, "")
    }

    fun addStepAtTheEnd(){
        _steps.add("")
    }

    fun updateSteps(index: Int, newValue: String){
        _steps[index] = newValue

    }


    fun removeStep(index: Int){
        if(_steps.size > 2) {
            _steps.removeAt(index)
        }
    }

    fun saveRecipe(recipe: UserRecipe, imageUri: Uri?) {
        _saveState.value = _saveState.value.copy(isSaving = true)
        viewModelScope.launch {
            try {
                if (imageUri != null) {
                    val downloadUrl = recipeRepo.uploadImageAndGetUrl(imageUri)
                    if (downloadUrl != null) {
                        recipe.recipeImage = downloadUrl // Ενημέρωση μόνο του πεδίου recipeImage
                    }
                }
                else{
                    recipe.recipeImage = "android.resource://com.TheCooker/${R.drawable.testmeal}"
                }

                recipeRepo.saveRecipe(recipe) // Αποθήκευση της συνταγής
                _saveState.value = _saveState.value.copy(
                    isSaving = false,
                    error = null,
                    saveSuccess = true
                )
            } catch (e: Exception) {
                _saveState.value = _saveState.value.copy(
                    isSaving = false,
                    error = "Error occurred ${e.message}",
                    saveSuccess = false
                )
            }
        }
    }






}