package com.example.newsapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(val topicText: String, val sports: Int) : Parcelable