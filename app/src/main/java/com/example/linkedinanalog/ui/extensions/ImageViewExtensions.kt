package com.example.linkedinanalog.ui.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.linkedinanalog.R

fun ImageView.loadAvatar(url: String) {
    Glide.with(this)
        .load(url)
        .placeholder(R.drawable.avatar_placeholder)
        .timeout(10_000)
        .circleCrop()
        .into(this)
}

fun ImageView.loadImage(url: String) {
    Glide.with(this)
        .load(url)
        .placeholder(R.drawable.ic_baseline_downloading_24)
        .timeout(10_000)
        .into(this)
}

