package ru.kepper104.notebuddy.presentation

import ru.kepper104.notebuddy.domain.model.Note

data class NoteState(
    val notes: List<Note> = emptyList(),
    val isEditing: Boolean = false,
    val lastDeletedNote: Note? = null,
    val isAbleToDeleteNotes: Boolean = true
)