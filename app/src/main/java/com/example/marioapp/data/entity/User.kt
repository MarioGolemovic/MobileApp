package com.example.marioapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) var uid: Int? = null,
    @ColumnInfo(name = "name") var playerName: String?,
    @ColumnInfo(name = "price") var playerPrice: String?,
    @ColumnInfo(name = "number") var playerNumber: String?,
    @ColumnInfo(name = "years") var playerYears: String?,
    @ColumnInfo(name = "position") var playerPosition: String?

)