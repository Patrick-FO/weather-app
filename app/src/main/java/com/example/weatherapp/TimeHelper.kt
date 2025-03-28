package com.example.weatherapp

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun isToday(date: Date): Boolean {
    val today = Calendar.getInstance()
    val calendar = Calendar.getInstance()
    calendar.time = date

    return (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR))
}

fun formatTimestampToHour(timestamp: Long): String {
    val date = Date(timestamp * 1000)
    val format = SimpleDateFormat("h a", Locale.getDefault())

    return format.format(date)
}

fun formatTimestampToWeekday(
    timestamp: Long,
    abbreviated: Boolean = false,
    useToday: Boolean = true
): String {
    val date = Date(timestamp * 1000)

    if(useToday && isToday(date)) {
        return "Today"
    }

    val pattern = if (abbreviated) "EEE" else "EEEE"
    val format = SimpleDateFormat(pattern, Locale.getDefault())

    return format.format(date)
}