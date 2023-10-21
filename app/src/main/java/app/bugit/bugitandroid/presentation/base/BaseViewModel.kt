package app.bugit.bugitandroid.presentation.base

import androidx.lifecycle.ViewModel
import dev.olshevski.navigation.reimagined.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<State, Event, Destination>: ViewModel(){
    protected abstract val privateState: MutableStateFlow<State>
    val state get() = privateState.asStateFlow()
    abstract var navController: NavController<Destination>
    abstract fun onEvent(event: Event)
}