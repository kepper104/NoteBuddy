package ru.kepper104.notebuddy.presentation

import androidx.compose.ui.graphics.Color
import ru.kepper104.notebuddy.domain.model.Note
import java.util.Date

data class NoteState(
    val notes: List<Note> = emptyList(),
    val isEditing: Boolean = false,
    val editedNote: Note = Note(null, "", "", Color.Gray, Date())
)