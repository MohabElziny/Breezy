package com.iti.mohab.breezy.util

import com.iti.mohab.breezy.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

fun getIcon(imageString: String): Int {
    var imageInInteger: Int = -1
    when (imageString) {
        "01d" -> imageInInteger = R.drawable.icon_01d
        "01n" -> imageInInteger = R.drawable.icon_01n
        "02d" -> imageInInteger = R.drawable.icon_02d
        "02n" -> imageInInteger = R.drawable.icon_02n
        "03n" -> imageInInteger = R.drawable.icon_03n
        "03d" -> imageInInteger = R.drawable.icon_03d
        "04d" -> imageInInteger = R.drawable.icon_04d
        "04n" -> imageInInteger = R.drawable.icon_04n
        "09d" -> imageInInteger = R.drawable.icon_09d
        "09n" -> imageInInteger = R.drawable.icon_09n
        "10d" -> imageInInteger = R.drawable.icon_10d
        "10n" -> imageInInteger = R.drawable.icon_10n
        "11d" -> imageInInteger = R.drawable.icon_11d
        "11n" -> imageInInteger = R.drawable.icon_11n
        "13d" -> imageInInteger = R.drawable.icon_13d
        "13n" -> imageInInteger = R.drawable.icon_13n
        "50d" -> imageInInteger = R.drawable.icon_50d
        "50n" -> imageInInteger = R.drawable.icon_50n
    }
    return imageInInteger
}

fun convertLongToTime(time: Long): String {

    val date = Date(TimeUnit.SECONDS.toMillis(time))
    val format = SimpleDateFormat("h:mm a")
    return format.format(date)
}

fun convertCalenderToDayString(calendar: Calendar): String {
    return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH)
}

fun convertCalenderToDayDate(calendar: Calendar): String {
    val date = Date(calendar.timeInMillis)
    val format = SimpleDateFormat("d MMM, yyyy")
    return format.format(date)
}
