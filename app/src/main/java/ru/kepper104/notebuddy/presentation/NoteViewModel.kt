package ru.kepper104.notebuddy.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Insert
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.kepper104.notebuddy.domain.model.Note
import ru.kepper104.notebuddy.domain.repository.NoteRepository
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
private val repository: NoteRepository
): ViewModel() {

    var state by mutableStateOf(NoteState())

    init {
        viewModelScope.launch {
            repository.getAllNotes().collectLatest { notes ->
                state = state.copy(
                    notes = notes
                )
            }
        }
    }

    fun enableEditing(note: Note){
        state = state.copy(
            isEditing = true,
            editedNote = note
        )
    }
    fun createNote(){
        enableEditing(Note())
    }
}