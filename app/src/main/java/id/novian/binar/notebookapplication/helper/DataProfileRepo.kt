package id.novian.binar.notebookapplication.helper

import android.content.Context
import id.novian.binar.notebookapplication.database.db.DataProfileDatabase
import id.novian.binar.notebookapplication.database.entities.DataProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DataProfileRepo(context: Context) {

    private val mDb = DataProfileDatabase.getInstance(context)

    suspend fun getDataProfile() = withContext(Dispatchers.IO){
            mDb?.dataProfileDao()?.getAllDataProfile()
    }

    suspend fun insertDataProfile(dataProfile: DataProfile) = withContext(Dispatchers.IO){
            mDb?.dataProfileDao()?.insertDataProfile(dataProfile)
    }

    suspend fun checkRegisteredProfile(email: String, password: String) = withContext(Dispatchers.IO){
            mDb?.dataProfileDao()?.getRegisteredDataProfile(email, password)
    }

    suspend fun getUsernameFromEmail(email: String) = withContext(Dispatchers.IO){
        mDb?.dataProfileDao()?.getUsernameFromEmail(email)
    }

    suspend fun getUsername(username: String) = withContext(Dispatchers.IO) {
        mDb?.dataProfileDao()?.getUsername(username)
    }

    suspend fun getEmail(email: String) = withContext(Dispatchers.IO) {
        mDb?.dataProfileDao()?.getEmail(email)
    }

    suspend fun getPassword(password: String) = withContext(Dispatchers.IO) {
        mDb?.dataProfileDao()?.getPassword(password)
    }
}