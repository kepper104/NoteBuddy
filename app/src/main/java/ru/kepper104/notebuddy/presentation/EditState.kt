package ru.kepper104.notebuddy.presentation

import androidx.compose.ui.graphics.Color

data class EditState(
    var titleText: String = "",
    var bodyText: String = "",
    var color: Color = Color.Gray,
    var colorText: String = "Gray"

)
