package ru.kepper104.notebuddy.presentation

data class EditState(
    var id: Int? = null,
    var titleText: String = "",
    var bodyText: String = "",
    var color: CustomColor = colorGreen,
    var colorsRadioButtonsList: MutableList<ToggleableInfo> = NoteColors.colors.map { c -> ToggleableInfo(color = c) }
        .toMutableList()

)
