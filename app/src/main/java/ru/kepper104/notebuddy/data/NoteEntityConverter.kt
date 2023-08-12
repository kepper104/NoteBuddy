package ru.kepper104.notebuddy.data

import androidx.compose.ui.graphics.Color
import ru.kepper104.notebuddy.domain.model.Note
import java.util.Date

fun NoteEntity.toNote(): Note {
    return Note(
        id, title, text, Color(color.toULong()), Date(last_modified)
    )
}
fun Note.toNoteEntity(): NoteEntity {
    return NoteEntity(
        id, title, text, color.value.toLong(), last_modified.time
    )
}