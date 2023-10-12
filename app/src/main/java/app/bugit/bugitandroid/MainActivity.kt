package app.bugit.bugitandroid

import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import app.bugit.bugitandroid.ui.theme.BugItAndroidTheme
import dagger.hilt.android.AndroidEntryPoint
import dev.olshevski.navigation.reimagined.NavBackHandler
import dev.olshevski.navigation.reimagined.NavHost
import dev.olshevski.navigation.reimagined.rememberNavController
import kotlinx.parcelize.Parcelize

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BugItAndroidTheme {
                RootNavGraph()
            }
        }
    }
}

@Composable
fun RootNavGraph(){
    val navController = rememberNavController<RootDestination>(startDestination = RootDestination.BugList)
    NavBackHandler(controller = navController)

    NavHost(navController) {destination ->
        when(destination){
            is RootDestination.BugList -> Box(){}
            is RootDestination.AddingBug -> Box(){}
        }
    }
}

sealed class RootDestination : Parcelable {
    @Parcelize
    object BugList : RootDestination()
    @Parcelize
    object AddingBug : RootDestination()
}