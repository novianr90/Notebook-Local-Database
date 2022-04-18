package id.novian.binar.notebookapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.novian.binar.notebookapplication.database.entities.Notes
import id.novian.binar.notebookapplication.helper.DataProfileRepo
import id.novian.binar.notebookapplication.helper.NotesRepo
import kotlinx.coroutines.launch

class UpdateViewModel(
    private val notesRepo: NotesRepo,
    private val dataProfileRepo: DataProfileRepo
) : ViewModel() {
    val noteValue = MutableLiveData<List<Notes>>()
    val insertData = MutableLiveData<Long>()

    val username = MutableLiveData<String>()
//    val username get() : LiveData<String> = _username

    val deleteNotes = MutableLiveData<Int>()
    val updateNotes = MutableLiveData<Int>()

    fun getUsername(email: String?) {
        viewModelScope.launch {
            username.value = dataProfileRepo.getUsernameFromEmail(email!!)
        }
    }

    fun getNotes(username: String) {
        viewModelScope.launch {
            noteValue.value = notesRepo.getNotes(username)
        }
    }

    fun saveNotes(notes: Notes) {
        viewModelScope.launch {
            insertData.value = notesRepo.insertNotes(notes)
        }
    }

    fun deletesNotes(notes: Notes) {
        viewModelScope.launch {
            deleteNotes.value = notesRepo.deleteNotes(notes)
        }
    }

    fun updatesNotes(notes: Notes) {
        viewModelScope.launch {
            updateNotes.value = notesRepo.updateNotes(notes)
        }
    }

}