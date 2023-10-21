package app.bugit.bugitandroid.presentation.graphs.root.screens.addingBug.utils

import android.net.Uri

sealed class AddingBugEvent{
    data class OnTitleChanged(val newText: String): AddingBugEvent()
    data class OnDescriptionChanged(val newText: String): AddingBugEvent()
    data class OnImageUriChanged(val newUri: Uri?): AddingBugEvent()
    object Submit: AddingBugEvent()
    object DismissAllDialog: AddingBugEvent()
}
