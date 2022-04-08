package id.novian.binar.notebookapplication.helper

import android.content.Context
import id.novian.binar.notebookapplication.database.db.DataProfileDatabase
import id.novian.binar.notebookapplication.database.entities.Notes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotesRepo(context: Context) {

    private val mDb = DataProfileDatabase.getInstance(context)

    suspend fun getNotes(username: String) = withContext(Dispatchers.IO) {
        mDb?.notesDao()?.getNotes(username)
    }

    suspend fun insertNotes(notes: Notes) = withContext(Dispatchers.IO) {
        mDb?.notesDao()?.insertNotes(notes)
    }

    suspend fun updateNotes(notes: Notes) = withContext(Dispatchers.IO) {
        mDb?.notesDao()?.updateNotes(notes)
    }

    suspend fun deleteNotes(notes: Notes) = withContext(Dispatchers.IO) {
        mDb?.notesDao()?.deleteNotes(notes)
    }
}