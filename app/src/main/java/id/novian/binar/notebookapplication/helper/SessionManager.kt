package id.novian.binar.notebookapplication.helper

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    var sharedPref : SharedPreferences = context.getSharedPreferences(SESSION, 0)
    private val editor = sharedPref.edit()

    fun setLogin(isLogin: Boolean){
        editor.apply {
            putBoolean(IS_LOGIN, isLogin)
            apply()
        }
    }

    fun isLogin(): Boolean {
        return sharedPref.getBoolean(IS_LOGIN, false)
    }

    fun setEmail(key: String, value: String){
        editor.apply {
            putString(key, value)
            apply()
        }
    }

    fun getLogin(key: String, value: String?): String? {
        return sharedPref.getString(key, value)
    }

    fun removeLogin(){
        editor.apply{
            clear()
            apply()
        }
    }

    companion object {
        const val SESSION = "shared_pref"
        const val IS_LOGIN = "login"
        const val EMAIL = "email"
    }
}