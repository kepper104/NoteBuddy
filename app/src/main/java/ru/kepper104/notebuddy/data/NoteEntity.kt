package ru.kepper104.notebuddy.data

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteEntity (
    @PrimaryKey var id: Int? = null,
    var title: String = "",
    var text: String = "",
    var color: Long = Color.Gray.value.toLong(),
    var last_modified: Long = 0
){
    constructor(): this(0, "", "", Color.Gray.value.toLong(), 0)

}
