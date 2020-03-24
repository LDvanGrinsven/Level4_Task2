package com.example.rps4

import android.content.Context


class RPS4Repository(context: Context) {

    private val rps4Dao: RPS4Dao

    init {
        val database = GameRoomDatabase.getDatabase(context)
        rps4Dao = database!!.gamesDao()
    }

    suspend fun getAllGames(): List<RPS4> {
        return rps4Dao.getAllGames()
    }

    suspend fun insertGames(product: RPS4) {
        rps4Dao.insertGames(product)
    }

    suspend fun deleteGames(product: RPS4) {
        rps4Dao.deleteGames(product)
    }

    suspend fun deleteAllGames() {
        rps4Dao.deleteAllGames()
    }
}
