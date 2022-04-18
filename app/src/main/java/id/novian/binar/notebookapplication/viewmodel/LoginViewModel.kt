package id.novian.binar.notebookapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.novian.binar.notebookapplication.database.entities.DataProfile
import id.novian.binar.notebookapplication.helper.DataProfileRepo
import kotlinx.coroutines.launch

class LoginViewModel(private val dataProfileRepo: DataProfileRepo) : ViewModel() {
    val emailAndPassword = MutableLiveData<List<DataProfile>>()
    val emailDb = MutableLiveData<String>()
    val passDb = MutableLiveData<String>()

    fun checkEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            emailAndPassword.value = dataProfileRepo.checkRegisteredProfile(email, password)
        }
    }

    fun checkEmail(email: String) {
        viewModelScope.launch {
            emailDb.value = dataProfileRepo.getEmail(email)
        }
    }

    fun checkPassword(password: String) {
        viewModelScope.launch {
            passDb.value = dataProfileRepo.getPassword(password)
        }
    }
}