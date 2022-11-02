package dev.ma7.fallingwords.view.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.ma7.fallingwords.view.util.Navigation
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _navigationSharedFlow = MutableSharedFlow<Navigation>()
    val navigationSharedFlow: SharedFlow<Navigation> = _navigationSharedFlow.asSharedFlow()

    fun navigate(navigation: Navigation) {
        viewModelScope.launch { _navigationSharedFlow.emit(navigation) }
    }
}