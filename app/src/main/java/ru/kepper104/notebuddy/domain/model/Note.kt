package ru.kepper104.notebuddy.domain.model

import androidx.compose.ui.graphics.Color
import java.util.Date

class Note (
    var id: Int? = null,
    var title: String = "",
    var text: String ="",
    var color: Color = Color.Gray,
    var last_modified: Date = Date()
)
