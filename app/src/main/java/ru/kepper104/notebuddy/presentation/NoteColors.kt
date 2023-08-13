package ru.kepper104.notebuddy.presentation

import androidx.compose.ui.graphics.Color

object NoteColors {
    val colors = listOf<Color>(Color.Blue, Color.LightGray, Color.Yellow, Color.Green)


}
fun colorToString(color: Color): String{
    return when(color){
        Color.Blue -> "Blue"
        Color.LightGray -> "Lightgray"
        Color.Yellow -> "Yellow"
        Color.Green -> "Green"
        else -> "None"
    }
}