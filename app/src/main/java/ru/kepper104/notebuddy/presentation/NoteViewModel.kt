package ru.kepper104.notebuddy.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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
    private val loggerTag = "LoggerViewModel"

    var mainState by mutableStateOf(NoteState())
    var editState by mutableStateOf(EditState())

    private val _sharedFlow = MutableSharedFlow<ScreenEvent>()
    val sharedFlow = _sharedFlow.asSharedFlow()

    sealed class ScreenEvent{
        object NoteSavedToast: ScreenEvent()
        object NoteDeletedSnackBar: ScreenEvent()
    }

    init {
        Log.d(loggerTag, "Initializing ViewModel")
        viewModelScope.launch {
            repository.getAllNotes().collectLatest { notes ->
                mainState = mainState.copy(
                    notes = notes
                )
            }
        }
    }

    fun enableEditing(note: Note){
        Log.d(loggerTag, "Enabling editing of note ${note.id}")

        mainState = mainState.copy(
            isEditing = true
        )
        editState = editState.copy(
            id = note.id,
            titleText = note.title,
            bodyText = note.text,
            color = note.color,
            colorText =  note.color.name()
        )
    }

    fun disableEditing(){
        Log.d(loggerTag, "Disabling editing")

        mainState = mainState.copy(
            isEditing = false
        )
    }

    fun createNote(){
        Log.d(loggerTag, "Creating new Note")

        enableEditing(Note())
    }

    fun saveNote(){
        Log.d(loggerTag, "Saving Note ${editState.id}")

        disableEditing()

        viewModelScope.launch {
            _sharedFlow.emit(ScreenEvent.NoteSavedToast)
            repository.insertNote(
                Note(editState.id, editState.titleText, editState.bodyText, editState.color, Date())
            )
        }

    }

    fun deleteNote(note: Note){
        Log.d(loggerTag, "Deleting Note ${note.id}")

        mainState = mainState.copy(
            lastDeletedNote = note
        )
        viewModelScope.launch {
            repository.deleteNote(note)
            _sharedFlow.emit(ScreenEvent.NoteDeletedSnackBar)

        }
    }

    fun restoreLastDeletedNote(){
        Log.d(loggerTag, "Restoring last Note")
        if (mainState.lastDeletedNote == null) return
        viewModelScope.launch{
            repository.insertNote(mainState.lastDeletedNote!!)
            mainState = mainState.copy(
                lastDeletedNote = null
            )
        }
    }

}


