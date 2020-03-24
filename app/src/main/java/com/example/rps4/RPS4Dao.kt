package com.example.rps4


import androidx.room.*


@Dao
interface RPS4Dao {

    @Query("SELECT * FROM games_table order by date DESC")
    suspend fun getAllGames(): List<RPS4>

    @Insert
    suspend fun insertGames(product: RPS4)

    @Delete
    suspend fun deleteGames(product: RPS4)

    @Query("DELETE FROM games_table")
    suspend fun deleteAllGames()

    @Query("SELECT COUNT(*) FROM games_table WHERE status=:status")
    fun getCountStatus(status:Int): Int

}
