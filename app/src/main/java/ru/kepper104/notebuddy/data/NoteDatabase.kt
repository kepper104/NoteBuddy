package ru.kepper104.notebuddy.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [NoteEntity::class],
    version = 2,
//    autoMigrations = [
//        AutoMigration (from = 1, to = 2)
//    ],
    exportSchema = true

)
abstract class NoteDatabase: RoomDatabase() {
    abstract val dao: NoteDatabaseDao
}