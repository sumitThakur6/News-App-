package com.example.newsapp.viewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.models.Article
import com.example.newsapp.models.NewsResponse
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.utils.Constants
import com.example.newsapp.utils.Network
import com.example.newsapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.http.Query
import java.io.IOException

class NewsViewModel(private val application : Application, private val repository : NewsRepository) : AndroidViewModel(application) {

    var breakingNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse : NewsResponse? = null

    var searchNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1;

    var categoryNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()

    init {
        getBreakingNews("in")
    }

    fun getBreakingNews(countryCode : String) = viewModelScope.launch {
        safeBreakingNewsCall(countryCode)
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
    }

    fun getCategoryViseNews(topic : String) = viewModelScope.launch {
        categoryNews.postValue(Resource.Loading())
        val response = repository.getNewsFromCategory("in", topic)
        categoryNews.postValue(handleCategoryNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                if(breakingNewsResponse == null){
                    breakingNewsResponse = resultResponse
                }
                else{
                    val oldArticle = breakingNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(message = response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success( resultResponse)
            }
        }
        return Resource.Error(message = response.message())
    }

    private fun handleCategoryNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(message = response.message())
    }

    fun saveArticle(article : Article) = viewModelScope.launch {
        repository.upsert(article)
    }

    fun getSavedNews() = repository.getSavedNews()

    fun deleteArticle(article : Article) = viewModelScope.launch {
        repository.deleteArticle(article)
    }

    private suspend fun safeBreakingNewsCall(countryCode: String){
        breakingNews.postValue(Resource.Loading())
       try{
           if(Network.hasInternetConnection(application)){
               val response = repository.getBreakingNews(countryCode, breakingNewsPage)
               breakingNews.postValue(handleBreakingNewsResponse(response))
           }
           else{
               breakingNews.postValue(Resource.Error("No Internet Connection"))
           }
       } catch (t : Throwable){
           when(t){
               is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
               else -> breakingNews.postValue(Resource.Error("Conversion Error"))
           }

       }
    }

    private suspend fun safeSearchNewsCall(searchQuery:  String){
        searchNews.postValue(Resource.Loading())
        try{
            if(Network.hasInternetConnection(application)){
                val response = repository.searchNews(searchQuery, searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            }
            else{
                searchNews.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t : Throwable){
            when(t){
                is IOException -> searchNews.postValue(Resource.Error("Network Failure"))
                else -> searchNews.postValue(Resource.Error("Conversion Error"))
            }

        }
    }
}
