package com.flores.dummydictionary.repository

import androidx.lifecycle.LiveData
import com.flores.dummydictionary.data.DummyDictionaryDatabase
import com.flores.dummydictionary.data.model.Word
import com.flores.dummydictionary.network.ApiResponse
import com.flores.dummydictionary.network.WordService
import retrofit2.HttpException
import java.io.IOException

class DictionaryRepository(
        database: DummyDictionaryDatabase,
        private val api: WordService,
) {
        private val wordDao = database.wordDao()

        suspend fun getAllWords(): ApiResponse<LiveData<List<Word>>> {
            return try {
                val response = api.getAllWord()

                if (response.count > 0){
                    wordDao.insertWord(response.words)
                }
                ApiResponse.Success(data = wordDao.getAllWords())
            }
            catch (e: HttpException){
                ApiResponse.Error(exception = e)
            }
            catch (e: IOException){
                ApiResponse.Error(exception = e)
            }
        }
        suspend fun addWord(word: Word) {
            wordDao.insertWord(word)
        }
}


