package com.example.a7minutesworkoutapp

import androidx.room.*
import com.example.roomdemo.HistoryEntity
import kotlinx.coroutines.flow.Flow

/* When you use the Room persistence library to store your app's data,
 you interact with the stored data by defining data access objects, or DAOs. */

@Dao
interface HistoryDao {

    // insert data fun
    @Insert
    // yes it is a coroutine function, cause inserting data may take some time
    suspend fun insert(historyEntity: HistoryEntity)

    // in query we type what kind of query it should be and inside it we type sql code
    // this query mean it will select all(*) entries from table
    @Query("SELECT * FROM `history-table`")

            /* In coroutines, a flow is a type that can emit multiple values sequentially,
            as opposed to suspend functions that return only a single value. For example,
            you can use a flow to receive live updates from a database. */

    // in our query we use fun that returns flow of our database
    fun fetchAllHistory(): Flow<List<HistoryEntity>>

}