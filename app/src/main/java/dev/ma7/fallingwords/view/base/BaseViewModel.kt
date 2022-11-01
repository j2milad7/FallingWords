package dev.ma7.fallingwords.view.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.ma7.fallingwords.view.util.Navigation
import dev.ma7.fallingwords.view.util.SingleEventLiveData

abstract class BaseViewModel : ViewModel() {

    private val _navigationLiveData = SingleEventLiveData<Navigation>()
    val navigationLiveData: LiveData<Navigation> = _navigationLiveData

    fun navigate(navigation: Navigation) {
        _navigationLiveData.value = navigation
    }
}