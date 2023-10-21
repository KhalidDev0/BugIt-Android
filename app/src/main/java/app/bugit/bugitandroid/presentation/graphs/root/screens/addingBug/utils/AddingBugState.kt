package app.bugit.bugitandroid.presentation.graphs.root.screens.addingBug.utils

import android.net.Uri

data class AddingBugState(
    val title: String = "",
    val description: String = "",
    val selectedImageUri: Uri? = null,
    val imageUrl: String = "",
    val showLoadingDialog: Boolean = false,
    val showSuccessDialog: Boolean = false,
    val showErrorDialog: Boolean = false,
    val dialogText: String = "",
)
