package id.novian.binar.notebookapplication.database.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.novian.binar.notebookapplication.database.dao.DataDao
import id.novian.binar.notebookapplication.database.dao.NotesDao
import id.novian.binar.notebookapplication.database.entities.DataProfile
import id.novian.binar.notebookapplication.database.entities.Notes

@Database(entities = [DataProfile::class, Notes::class], version = 1)
abstract class DataProfileDatabase: RoomDatabase() {
    abstract fun dataProfileDao(): DataDao
    abstract fun notesDao(): NotesDao

    companion object {
        private var INSTANCE : DataProfileDatabase? = null

        fun getInstance(context: Context): DataProfileDatabase? {
            if (INSTANCE == null){
                synchronized(DataProfileDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        DataProfileDatabase::class.java,
                    "DataProfile.db").build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance(){
            INSTANCE = null
        }
    }
}