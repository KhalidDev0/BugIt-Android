package app.bugit.bugitandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import app.bugit.bugitandroid.presentation.graphs.root.graph.RootNavGraph
import app.bugit.bugitandroid.ui.theme.BugItAndroidTheme
import dagger.hilt.android.AndroidEntryPoint

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