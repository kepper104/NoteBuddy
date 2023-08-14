package ru.kepper104.notebuddy.presentation

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.kepper104.notebuddy.domain.model.Note
import ru.kepper104.notebuddy.domain.repository.NoteRepository
import java.util.Date
import javax.inject.Inject



@HiltViewModel
class NoteViewModel @Inject constructor(
private val repository: NoteRepository
): ViewModel() {

    var mainState by mutableStateOf(NoteState())
    var editState by mutableStateOf(EditState())

    private val statusMessage = MutableLiveData<Event<String>>()

    val message : LiveData<Event<String>>
        get() = statusMessage


    init {
        viewModelScope.launch {
            repository.getAllNotes().collectLatest { notes ->
                mainState = mainState.copy(
                    notes = notes
                )
            }
        }
    }

    fun enableEditing(note: Note){
        mainState = mainState.copy(
            isEditing = true,
            editedNote = note
        )
    }
    fun disableEditing(){
        mainState = mainState.copy(
            isEditing = false
        )
    }
    fun createNote(){
        enableEditing(Note())
    }
    fun saveNote(){
        disableEditing()
        statusMessage.value = Event("Note Saved!")
        viewModelScope.launch {
            repository.insertNote(
                Note(null, editState.titleText, editState.bodyText, editState.color, Date())
            )
        }

    }
    fun deleteNote(note: Note){
        
    }
    fun callToast(message: String){
        statusMessage.value = Event(message)
    }

}


// Thanks to stackoverflow
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}