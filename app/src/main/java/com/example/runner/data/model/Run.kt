package com.example.runner.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "run")
data class Run (

    val distance: Double,
    val maxSpeed: Double,
    val averageSpeed: Double,
    val time: String,
    val date: String

) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
