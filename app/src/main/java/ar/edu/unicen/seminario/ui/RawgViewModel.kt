package ar.edu.unicen.seminario.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unicen.seminario.ddl.data.Rawg.RawgRepository
import ar.edu.unicen.seminario.ddl.models.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RawgViewModel @Inject constructor(
private val rawgRepository: RawgRepository
) : ViewModel(){
    private val _loading = MutableStateFlow(false)
    val loading  = _loading.asStateFlow()

    private val _error = MutableStateFlow(false)
    val error = _error.asStateFlow()
    private val _games = MutableStateFlow<List<Game>>(emptyList())
    val games = _games.asStateFlow()



    fun getFilteredGames(platforms: String?, genres: String?, ordering: String?, search: String? = null) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = false

            val result = rawgRepository.getFilteredGames(platforms, genres, ordering, search)
            _games.value = result
            _loading.value = false
            _error.value = result.isEmpty()
        }
    }
    fun searchGames(query: String) {
        // Reutiliza getFilteredGames, solo pasando el parámetro 'search'
        getFilteredGames(null, null, null, query.takeIf { it.isNotBlank() })
    }
    fun getAllGames() {
        getFilteredGames(null, null, null)
    }

}