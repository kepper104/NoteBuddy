package ru.kepper104.notebuddy

import android.content.Context
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDate(date: Date, pattern: String): String{
    val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    return simpleDateFormat.format(date)
}


fun makeToast(message: String, ctx: Context){
    Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
}

fun cutoffText(text: String, cutoffLength: Int): String {
    if (text.length <= cutoffLength) return text
    return text.substring(0, cutoffLength - 3).plus("...")
}