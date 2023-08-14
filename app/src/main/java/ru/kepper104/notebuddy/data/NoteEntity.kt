package ru.kepper104.notebuddy.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteEntity (
    @PrimaryKey var id: Int? = null,
    var title: String = "",
    var text: String = "",
    var colorID: Int = 0,
    var last_modified: Long = 0
){
    constructor(): this(0, "", "", 0, 0)

}
