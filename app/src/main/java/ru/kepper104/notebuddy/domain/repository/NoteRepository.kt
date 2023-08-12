package ru.kepper104.notebuddy.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.kepper104.notebuddy.domain.model.Note

interface NoteRepository {
    suspend fun insertNote(note: Note)

    suspend fun deleteNote(note: Note)

    fun getAllNotes(): Flow<List<Note>>

}