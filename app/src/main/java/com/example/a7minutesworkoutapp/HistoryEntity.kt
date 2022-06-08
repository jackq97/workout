package com.example.roomdemo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/* first we use annotation(metadata for our data base) to mark out table as an entity
* marking our data class as an entity and then naming it */

// make sure to explicitly mention the data type for our column entries

@Entity(tableName = "history-table")
data class HistoryEntity(
    // primary key gives unique id to each of our entry
    @PrimaryKey()
    var date: String = "",

)
