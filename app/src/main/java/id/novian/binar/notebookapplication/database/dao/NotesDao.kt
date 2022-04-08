package id.novian.binar.notebookapplication.database.dao

import androidx.room.*
import id.novian.binar.notebookapplication.database.entities.Notes

@Dao
interface NotesDao {
    @Query("SELECT * FROM Notes WHERE data_username = :username")
    fun getNotes(username: String): List<Notes>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotes(notes: Notes): Long

    @Update
    fun updateNotes(notes: Notes): Int

    @Delete
    fun deleteNotes(notes: Notes): Int
}