package ru.kepper104.notebuddy.presentation

import androidx.compose.ui.graphics.Color

val colorGreen = CustomColor("Green", Color(170, 200, 167, 255))
val colorPink = CustomColor("Pink", Color(255, 155, 155, 255))
val colorBlue = CustomColor("Blue", Color(182, 255, 255, 255))
val colorYellow = CustomColor("Yellow", Color(255, 205, 156, 255))
val colorGray = CustomColor("Background Gray", Color(51, 51, 51, 255))
val colorLightGray = CustomColor("Background Gray", Color(211, 211, 211, 255))


object NoteColors {
    val colors = listOf<CustomColor>(colorGreen, colorPink, colorBlue, colorYellow)


}
class CustomColor(private val name: String, val color: Color){
    fun name(): String{
        return name
    }

    fun c(): Color{
        return color
    }

}