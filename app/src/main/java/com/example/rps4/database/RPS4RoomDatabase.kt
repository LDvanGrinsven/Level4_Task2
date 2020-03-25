package com.example.rps4.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.rps4.model.RPS4

@Database(entities = [RPS4::class], version = 1, exportSchema = false)
abstract class GameRoomDatabase : RoomDatabase() {

    abstract fun gamesDao(): RPS4Dao

    companion object {
        private const val DATABASE_NAME = "GameDatabase"

        @Volatile
        private var GameRoomDatabaseInstance: GameRoomDatabase? = null

        fun getDatabase(context: Context): GameRoomDatabase? {
            if (GameRoomDatabaseInstance == null) {
                synchronized(GameRoomDatabase::class.java) {
                    if (GameRoomDatabaseInstance == null) {
                        GameRoomDatabaseInstance =
                            Room.databaseBuilder(context.applicationContext,GameRoomDatabase::class.java, DATABASE_NAME).build()
                    }
                }
            }
            return GameRoomDatabaseInstance
        }
    }

}
