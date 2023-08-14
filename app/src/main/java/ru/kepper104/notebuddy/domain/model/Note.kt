package ru.kepper104.notebuddy.domain.model

import ru.kepper104.notebuddy.presentation.CustomColor
import ru.kepper104.notebuddy.presentation.colorGreen
import java.util.Date

class Note (
    var id: Int? = null,
    var title: String = "",
    var text: String ="",
    var color: CustomColor = colorGreen,
    var last_modified: Date = Date()
)
