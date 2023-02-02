package com.example.linkedinanalog.ui.extensions

import android.widget.TextView


fun TextView.parseDateTime(stringDate: String) {
    val data = stringDate.split("T")[0]
    val time = stringDate.substringAfter("T").chunked(5)[0]
    val result = "$data $time"
    this.text = result

}


fun TextView.parseDate(stringDate: String) {
    this.text = stringDate.split("T")[0]
}