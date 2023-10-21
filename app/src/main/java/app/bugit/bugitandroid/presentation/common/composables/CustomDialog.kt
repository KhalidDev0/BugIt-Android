package app.bugit.bugitandroid.presentation.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun CustomDialog(
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    onDismissRequest: () -> Unit,
    body: @Composable () -> Unit
) {
    if (enabled)
        Dialog(onDismissRequest = onDismissRequest) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .clip(shape = CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                body()
            }
        }
}