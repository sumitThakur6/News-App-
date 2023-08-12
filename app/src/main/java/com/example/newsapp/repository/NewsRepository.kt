package com.example.newsapp.repository

import com.example.newsapp.apiServices.RetrofitHelper
import com.example.newsapp.db.ArticleDatabase
import com.example.newsapp.models.Article

class NewsRepository(val db : ArticleDatabase) {

    suspend fun getBreakingNews(countryCode : String, page : Int) = RetrofitHelper.api.getNews(countryCode, page)

    suspend fun searchNews(query: String, page : Int) = RetrofitHelper.api.searchForNews(query, page)

    suspend fun getNewsFromCategory(countryCode : String, topic : String)  = RetrofitHelper.api.getNewsFromCategory(countryCode, topic)

    suspend fun upsert(article : Article) = db.getArticleDao().upsert(article)

    suspend fun deleteArticle(article : Article) = db.getArticleDao().deleteArticle(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

}