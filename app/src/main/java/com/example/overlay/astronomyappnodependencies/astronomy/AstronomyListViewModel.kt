package com.example.overlay.astronomyappnodependencies.astronomy

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.overlay.astronomyappnodependencies.util.CoroutineContextDispatcherProvider
import com.example.overlay.astronomyappnodependencies.util.CoroutineContextProvider
import com.example.overlay.astronomyappnodependencies.util.Result
import com.example.overlay.astronomyappnodependencies.network.api.AstronomyPicture
import com.example.overlay.astronomyappnodependencies.data.AstronomyRepository
import com.example.overlay.astronomyappnodependencies.data.AstronomyRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Immutable
sealed class AstronomyListViewState {
    data object Loading : AstronomyListViewState()
    data class Content(val list: List<AstronomyPicture>) : AstronomyListViewState()
    data class Error(val error: String) : AstronomyListViewState()
}

class AstronomyListViewModel(
    private val repository: AstronomyRepository = AstronomyRepositoryImpl(),
    private val contextPool: CoroutineContextProvider = CoroutineContextDispatcherProvider()
): ViewModel() {

    private val _viewState = MutableStateFlow<AstronomyListViewState>(AstronomyListViewState.Loading)
    val viewState: StateFlow<AstronomyListViewState> = _viewState.asStateFlow()

    init {
        viewModelScope.launch(contextPool.ioDispatcher) {
            when (val initialLoad = repository.retrieveAstronomyPictures()) {
                is Result.Success -> withContext(contextPool.mainDispatcher) {
                    _viewState.tryEmit(AstronomyListViewState.Content(initialLoad.data))
                }
                is Result.Failure -> withContext(contextPool.mainDispatcher) {
                    val errorMessage = "Unable to load message. Cause: ${initialLoad.error?.message}"
                    _viewState.tryEmit(AstronomyListViewState.Error(errorMessage))
                }
            }
        }
    }
}