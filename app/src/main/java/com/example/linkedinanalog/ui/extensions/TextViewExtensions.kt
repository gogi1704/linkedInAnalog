package com.example.linkedinanalog.ui.extensions

import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

fun TextView.parseDateTime(stringDate: String) {
    val data = stringDate.split("T")[0]
    val time =stringDate.substringAfter("T").chunked(5)[0]
    this.text = "$data $time"

}


fun TextView.parseDate(stringDate: String) {
    this.text = stringDate.split("T")[0]

    fun TextView.parseTime(stringTime: String) {

    }

}