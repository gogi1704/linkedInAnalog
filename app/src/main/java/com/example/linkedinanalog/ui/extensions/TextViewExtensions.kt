package com.example.linkedinanalog.ui.extensions

import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

fun TextView.parseDateTime(stringDate: String) {
    val dataFormat = SimpleDateFormat("yy-MM-dd'T'HH:mm", Locale("ru"))
    this.text = dataFormat.parse(stringDate)?.toString() ?: ""

}


fun TextView.parseDate(stringDate: String?) {
    this.text = stringDate?.split("T")?.get(0) ?: "error"

}