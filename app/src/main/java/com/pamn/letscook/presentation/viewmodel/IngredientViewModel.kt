package com.pamn.letscook.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pamn.letscook.data.repositories.IngredientRepository
import com.pamn.letscook.domain.models.Ingredient
import com.pamn.letscook.domain.usecases.IngredientInitializer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class IngredientViewModel(
    private val repository: IngredientRepository,
    private val ingredientInitializer: IngredientInitializer
) : ViewModel() {
    // Estados para la lista de ingredientes y el indicador de carga
    private val _ingredients = MutableStateFlow<List<Ingredient>>(emptyList())
    val ingredients: StateFlow<List<Ingredient>> get() = _ingredients

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    // Estado para manejar errores de manera explícita
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    fun initializeIngredients() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                ingredientInitializer.initializeIngredientsIfEmpty()
                loadIngredients() // Carga los datos actualizados
            } catch (e: Exception) {
                _errorMessage.value = "Error al inicializar ingredientes: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // para obtener los datos en una lista y mantener el estado
    // carga todos los ingredientes
    fun loadIngredients() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null // Limpiar el estado de error anterior

            repository.getAllIngredients()
                .onSuccess { ingredients ->
                    _ingredients.value = ingredients
                }
                .onFailure { error ->
                    //println(vv"Error al cargar ingredientes: ${error.message}")
                    handleIngredientError(error)
                }
            _isLoading.value = false
        }
    }

    // Función para guardar un ingrediente
    fun saveIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null // Limpiar el estado de error anterior

            repository.saveIngredient(ingredient)
                .onSuccess {
                    loadIngredients() // Refrescar la lista tras guardar
                }
                .onFailure { error ->
                    handleIngredientError(error)
                }

            _isLoading.value = false
        }
    }

    // Función para guardar múltiples ingredientes
    fun saveIngredients(ingredients: List<Ingredient>) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null // Limpiar el estado de error anterior

            repository.saveIngredients(ingredients)
                .onSuccess {
                    loadIngredients() // Refrescar la lista tras guardar
                }
                .onFailure { error ->
                    handleIngredientError(error)
                }

            _isLoading.value = false
        }
    }

    // Función para cargar un ingrediente por nombre
    fun loadIngredientByName(name: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null // Limpiar el estado de error anterior

            repository.getIngredientByName(name)
                .onSuccess { ingredient ->
                    // Aquí puedes manejar el ingrediente individual (opcional)
                    _ingredients.value = listOf(ingredient)
                }
                .onFailure { error ->
                    handleIngredientError(error)
                }

            _isLoading.value = false
        }
    }

    // Función para manejar errores y actualizar el estado
    private fun handleIngredientError(error: Throwable) {
        when (error) {
            is IngredientRepository.IngredientError.NetworkError -> {
                _errorMessage.value = "Error de red: verifica tu conexión a internet."
            }
            is IngredientRepository.IngredientError.DatabaseError -> {
                _errorMessage.value = "Error de base de datos: no se pudieron procesar los datos."
            }
            is IngredientRepository.IngredientError.NotFoundError -> {
                _errorMessage.value = error.message ?: "No se encontraron ingredientes."
            }
            is IngredientRepository.IngredientError.ParseError -> {
                _errorMessage.value = error.message ?: "Error al interpretar los datos."
            }
            else -> {
                _errorMessage.value = "Error desconocido: ${error.message}"
            }
        }

        // Opcional: Imprimir el error en el log para depuración
        println("Error en ViewModel: ${error.message}")
    }

    // Sin el factory method
    // De prueba por simplicidad
    /**
     * No se usa pues la lógica de creación del IngredientViewModel queda acoplada directamente a la clase misma.
     * Esto dificultaría el uso de frameworks de inyección de dependencias como Hilt o Dagger,
     * que suelen esperar el ViewModelProvider.Factory
     * Rompe con el principio de Open/Closed de SOLID
     */
    /*
    companion object {
        @Provides
        fun provideIngredientViewModel(
            ingredientRepository: IngredientRepository
        ): IngredientViewModel {
            return IngredientViewModel(ingredientRepository)
        }
    }
     */

}