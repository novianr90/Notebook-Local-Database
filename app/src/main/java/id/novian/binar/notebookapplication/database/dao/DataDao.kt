package id.novian.binar.notebookapplication.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import id.novian.binar.notebookapplication.database.entities.DataProfile

@Dao
interface DataDao {
    //Get Registered Acc
    @Query("SELECT * FROM DataProfile WHERE email = :email AND password = :password")
    fun getRegisteredDataProfile(email: String, password: String): List<DataProfile>

    @Query("SELECT username FROM DataProfile WHERE username = :username")
    fun getUsername(username: String): String

    @Query("SELECT email FROM DataProfile WHERE email = :email")
    fun getEmail(email: String): String

    @Query("SELECT username FROM DataProfile WHERE email = :email")
    fun getUsernameFromEmail(email: String): String

    //Get All Data
    @Query("SELECT * FROM DataProfile")
    fun getAllDataProfile(): List<DataProfile>

    //Insert
    @Insert(onConflict = REPLACE)
    fun insertDataProfile(dataProfile: DataProfile): Long
}