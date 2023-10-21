package app.bugit.bugitandroid.presentation.graphs.root.graph

import android.os.Parcelable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.bugit.bugitandroid.presentation.graphs.root.screens.addingBug.view.AddingBugScreen
import app.bugit.bugitandroid.presentation.graphs.root.screens.addingBug.viewModel.AddingBugViewModel
import dev.olshevski.navigation.reimagined.NavBackHandler
import dev.olshevski.navigation.reimagined.NavHost
import dev.olshevski.navigation.reimagined.hilt.hiltViewModel
import dev.olshevski.navigation.reimagined.rememberNavController
import kotlinx.parcelize.Parcelize

@Composable
fun RootNavGraph(){
    val navController = rememberNavController<RootDestination>(startDestination = RootDestination.AddingBug)
    NavBackHandler(controller = navController)

    NavHost(navController) {destination ->
        when(destination){
            is RootDestination.BugList -> Box(){}

            is RootDestination.AddingBug -> {
                val viewModel = hiltViewModel<AddingBugViewModel>()
                val state by viewModel.state.collectAsState()
                viewModel.navController = navController

                AddingBugScreen(
                    state = state,
                    onEvent = viewModel::onEvent
                )
            }
        }
    }
}

sealed class RootDestination : Parcelable {
    @Parcelize
    object BugList : RootDestination()
    @Parcelize
    object AddingBug : RootDestination()
}