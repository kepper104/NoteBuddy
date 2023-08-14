package ru.kepper104.notebuddy

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDate(date: Date, pattern: String): String{
    val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    return simpleDateFormat.format(date)
}