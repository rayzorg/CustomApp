package com.example.mainactivity.jsonclasses

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)