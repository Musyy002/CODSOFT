package com.example.quoteapp

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SharedPreferencesHelper {

    private const val PREFS_NAME = "quote_prefs"
    private const val FAVORITE_QUOTES_KEY = "favorite_quotes"

    fun saveFavoriteQuote(context: Context, quote: Quote) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val quotesList = getFavoriteQuotes(context).toMutableList()
        quotesList.add(quote)

        val json = Gson().toJson(quotesList)
        editor.putString(FAVORITE_QUOTES_KEY, json)
        editor.apply()

        // Log for debugging
        Log.d("SharedPreferencesHelper", "Quote saved: $json")
    }

    fun getFavoriteQuotes(context: Context): List<Quote> {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(FAVORITE_QUOTES_KEY, null)

        Log.d("SharedPreferencesHelper", "Retrieved JSON: $json")

        return if (json != null) {
            try {
                val type = object : TypeToken<List<Quote>>() {}.type
                val quotesList: List<Quote> = Gson().fromJson(json, type)

                // Log the retrieved list for verification
                Log.d("SharedPreferencesHelper", "Deserialized list: $quotesList")

                quotesList
            } catch (e: Exception) {
                Log.e("SharedPreferencesHelper", "Error deserializing JSON", e)
                emptyList()
            }
        } else {
            emptyList()
        }
    }



}
