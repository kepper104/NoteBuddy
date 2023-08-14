package ru.kepper104.notebuddy.data

import ru.kepper104.notebuddy.domain.model.Note
import ru.kepper104.notebuddy.presentation.NoteColors
import java.util.Date

fun NoteEntity.toNote(): Note {
    return Note(
        id, title, text, NoteColors.colors[colorID], Date(last_modified)
    )
}
fun Note.toNoteEntity(): NoteEntity {
    return NoteEntity(
        id, title, text, NoteColors.colors.indexOf(color), last_modified.time
    )
}