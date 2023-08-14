package ru.kepper104.notebuddy.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kepper104.notebuddy.domain.model.Note
import ru.kepper104.notebuddy.domain.repository.NoteRepository

class NoteRepositoryImplementation(
    private val dao: NoteDatabaseDao
) : NoteRepository {
    override suspend fun insertNote(note: Note) {
        dao.insertNote(note.toNoteEntity())
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note.toNoteEntity())
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return dao.getAllNotes().map { notes ->
            notes.map { it.toNote() }
         }
    }

}