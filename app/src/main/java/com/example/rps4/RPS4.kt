package com.example.rps4

import androidx.room.*


@Entity(tableName = "games_table")

class RPS4(
    @ColumnInfo(name = "computerPick")
    var computerPick: Int,
    @ColumnInfo(name = "userPick")
    var userPick: Int,
    @ColumnInfo(name = "status")
    var status: Int,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "date")
    var date: Long
)


