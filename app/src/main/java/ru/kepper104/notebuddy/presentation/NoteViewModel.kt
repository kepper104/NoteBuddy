package ru.kepper104.notebuddy.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    fun createNote(){
        enableEditing(Note())
    }
    suspend fun saveNote(){
        repository.insertNote(
            Note(null, editState.titleText, editState.bodyText, editState.color, Date())
        )
    }
}