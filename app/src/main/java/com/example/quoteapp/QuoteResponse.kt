package com.example.quoteapp

import retrofit2.Call
import retrofit2.http.GET

data class QuoteResponse(
    val q: String, // Quote text
    val a: String  // Author
)


interface ApiService {
    @GET("random")
    fun getQuote(): Call<List<QuoteResponse>>

}
