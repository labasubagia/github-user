package com.example.githubsearch.model

data class Search(
    val total_count: Int = 0,
    val incomplete_results: Boolean? = null,
    val items: ArrayList<User>
)