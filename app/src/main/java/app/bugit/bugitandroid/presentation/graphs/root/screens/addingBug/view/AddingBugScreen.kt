package app.bugit.bugitandroid.presentation.graphs.root.screens.addingBug.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.bugit.bugitandroid.presentation.common.composables.CustomDialog
import app.bugit.bugitandroid.presentation.graphs.root.screens.addingBug.utils.AddingBugEvent
import app.bugit.bugitandroid.presentation.graphs.root.screens.addingBug.utils.AddingBugState
import app.bugit.bugitandroid.presentation.graphs.root.screens.addingBug.viewModel.AddingBugViewModel
import coil.compose.AsyncImage
import dev.olshevski.navigation.reimagined.hilt.hiltViewModel

@Composable
fun AddingBugScreen(
    state: AddingBugState,
    onEvent: (AddingBugEvent) -> Unit
) {
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> onEvent(AddingBugEvent.OnImageUriChanged(uri)) }
    )

    val intent = (LocalContext.current as Activity).intent
    LaunchedEffect(intent) {
        if (Intent.ACTION_SEND == intent.action && intent.type?.startsWith("image/") == true) {
            val imageUri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
            if (imageUri != null) {
                onEvent(AddingBugEvent.OnImageUriChanged(imageUri))
            }
        }
    }

    CustomDialog(
        enabled = state.showLoadingDialog,
        onDismissRequest = {}
    ) {
        CircularProgressIndicator(color = Color.White)
    }

    CustomDialog(
        enabled = state.showSuccessDialog || state.showErrorDialog,
        onDismissRequest = { onEvent(AddingBugEvent.DismissAllDialog) }
    ) {
        Text(text = state.dialogText, color = Color.White)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Text(text = "BugIt", color = Color.Black, fontSize = 20.sp)

        Box(modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .border(2.dp, Color.Black)
            .clickable { singlePhotoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) })
        {
            AsyncImage(
                model = state.selectedImageUri,
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
        }

        TextField(
            value = state.title,
            onValueChange = {onEvent(AddingBugEvent.OnTitleChanged(it))},
            label = { Text(text = "Title") },
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        TextField(
            value = state.description,
            onValueChange = {onEvent(AddingBugEvent.OnDescriptionChanged(it))},
            label = { Text(text = "Description") },
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Button(onClick = { onEvent(AddingBugEvent.Submit) }) {
            Text(text = "Submit")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddingBugScreenPreview(){
    val viewModel = hiltViewModel<AddingBugViewModel>()

    AddingBugScreen(
        state = viewModel.state.collectAsState().value,
        onEvent = viewModel::onEvent
    )
}