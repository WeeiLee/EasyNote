package com.example.easynote.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class CalendarEvent(
    val id: Int?,
    val title: String,
    val description: String,
    val date: LocalDate
) : Parcelable
