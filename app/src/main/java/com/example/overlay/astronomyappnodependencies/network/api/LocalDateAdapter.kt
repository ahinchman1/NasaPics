package com.example.overlay.astronomyappnodependencies.network.api

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate

class LocalDateAdapter {

    @ToJson
    fun toJson(date: LocalDate): String = date.toString()

    @FromJson
    fun fromJson(date: String): LocalDate = LocalDate.parse(date)
}