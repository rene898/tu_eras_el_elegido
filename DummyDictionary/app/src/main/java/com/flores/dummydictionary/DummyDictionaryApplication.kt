package com.flores.dummydictionary

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.flores.dummydictionary.data.DummyDictionaryDatabase
import com.flores.dummydictionary.network.RetrofitInstance
import com.flores.dummydictionary.network.RetrofitInstance.getWordServices
import com.flores.dummydictionary.network.RetrofitInstance.setToken
import com.flores.dummydictionary.repository.DictionaryRepository
import com.flores.dummydictionary.repository.LoginRepository

class DummyDictionaryApplication : Application() {
    private val prefs: SharedPreferences by lazy {
        getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
    }

    private val dataBase by lazy {
        DummyDictionaryDatabase.getInstance(this)
    }
    private fun getAPIService() = with(RetrofitInstance){
        setToken(getToken())
        getWordServices()
    }

    fun getDictionaryRepository() = with(dataBase) {
        DictionaryRepository(dataBase, getAPIService())
    }

    fun getLoginRepository() =
        LoginRepository(getAPIService())

    private fun getToken(): String = prefs.getString(USER_TOKEN, "")!!

    fun isUserLogin() = getToken() != ""

    fun saveAuthToken(token: String){
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    companion object {
        const val USER_TOKEN = "user_token"
    }

}