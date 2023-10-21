package app.bugit.bugitandroid.presentation.graphs.root.screens.addingBug.viewModel

import androidx.lifecycle.viewModelScope
import app.bugit.bugitandroid.presentation.base.BaseViewModel
import app.bugit.bugitandroid.presentation.graphs.root.graph.RootDestination
import app.bugit.bugitandroid.presentation.graphs.root.screens.addingBug.utils.AddingBugEvent
import app.bugit.bugitandroid.presentation.graphs.root.screens.addingBug.utils.AddingBugState
import app.bugit.networklayer.data.model.Resource
import app.bugit.networklayer.domain.googleSheet.useCase.UploadBugToGoogleSheetUseCase
import app.bugit.networklayer.domain.image.useCase.GetImageUrlUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.olshevski.navigation.reimagined.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AddingBugViewModel @Inject constructor(
    private val uploadBugToGoogleSheetUseCase: UploadBugToGoogleSheetUseCase,
    private val getImageUrlUseCase: GetImageUrlUseCase,
): BaseViewModel<AddingBugState, AddingBugEvent, RootDestination>() {
    override val privateState = MutableStateFlow(AddingBugState())
    override lateinit var navController: NavController<RootDestination>

    override fun onEvent(event: AddingBugEvent) {
        privateState.apply{
            when (event) {
                is AddingBugEvent.OnTitleChanged -> value = privateState.value.copy(title = event.newText)
                is AddingBugEvent.OnDescriptionChanged -> value = privateState.value.copy(description = event.newText)
                is AddingBugEvent.OnImageUriChanged -> value = privateState.value.copy(selectedImageUri = event.newUri)
                is AddingBugEvent.Submit -> submit()
                is AddingBugEvent.DismissAllDialog -> value = privateState.value.copy(showErrorDialog = false, showSuccessDialog = false, showLoadingDialog = false)
            }
        }
    }

    private fun submit(){
        if (privateState.value.title.isNullOrEmpty()
            || privateState.value.description.isNullOrEmpty()
            || privateState.value.selectedImageUri == null
        ){
            privateState.value = privateState.value.copy(showErrorDialog = true, dialogText = "Please fill all required fields")
            return
        }

        getImageUrlUseCase(
            imageUri = privateState.value.selectedImageUri!!
        ).onEach {resource ->
            privateState.apply {
                when (resource){
                    is Resource.Loading -> {
                        onEvent(AddingBugEvent.DismissAllDialog)
                        value = value.copy(showLoadingDialog = true)
                    }
                    is Resource.Success -> {
                        value = value.copy(imageUrl = resource.data.data.displayUrl)
                        uploadBug()
                    }
                    is Resource.Error -> {
                        onEvent(AddingBugEvent.DismissAllDialog)
                        value = value.copy(showErrorDialog = true, dialogText = resource.error.message)
                    }
                }
            }

        }.launchIn(viewModelScope)
    }

    private fun uploadBug(){
        uploadBugToGoogleSheetUseCase(
            title = privateState.value.title,
            description = privateState.value.description,
            imageUrl = privateState.value.imageUrl,
        ).onEach { resource ->
            privateState.apply {
                when (resource) {
                    is Resource.Loading -> Unit
                    is Resource.Success -> {
                        onEvent(AddingBugEvent.DismissAllDialog)
                        value = value.copy(showSuccessDialog = true, dialogText = resource.data.message)
                    }
                    is Resource.Error -> {
                        onEvent(AddingBugEvent.DismissAllDialog)
                        value = value.copy(showErrorDialog = true, dialogText = resource.error.message)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}