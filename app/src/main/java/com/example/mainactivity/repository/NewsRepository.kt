package com.example.mainactivity.repository

import com.example.mainactivity.db.ArticleDatabase
import com.example.mainactivity.newsapi.RetrofitInstance

class NewsRepository (val db: ArticleDatabase){
    suspend fun getBreakingNews(countryCode: String,pageNumber:Int)=
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)

    suspend fun searchNews(searchQuery:String,pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery,pageNumber)
}