package com.example.quoteapp

import android.content.Context
import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.quoteapp.SharedPreferencesHelper  // Make sure to import your helper class
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var currentQuote: String  // Holds the current quote text
    private lateinit var currentAuthor: String // Holds the current quote author

    private lateinit var quoteTextView: TextView
    private lateinit var authorTextView: TextView

    lateinit var bottomNav : BottomNavigationView

    private val sharedPreferencesKey = "favorite_quotes_key"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottomNavigationView) as BottomNavigationView
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.favfrag -> {
                    loadFragment(FavFragment())

                    val toolbar = findViewById<MaterialToolbar>(R.id.materialToolbar)
                    toolbar.visibility= View.GONE
                    val qcard = findViewById<MaterialCardView>(R.id.quoteCard)
                    qcard.visibility=View.GONE
                    val btns = findViewById<LinearLayout>(R.id.btnslayout)
                    btns.visibility=View.GONE
                    val save = findViewById<Button>(R.id.saveButton)
                    save.visibility = View.GONE

                    true
                }

                R.id.homefrag -> {
                    startActivity(Intent(this,MainActivity::class.java))
                    true

                }

                else -> {
                    false

                }
            }
        }



        quoteTextView = findViewById(R.id.quoteTextView)
        authorTextView = findViewById(R.id.authorTextView)

        currentQuote = quoteTextView.text.toString()
        currentAuthor = authorTextView.text.toString()

        // Display a new quote on startup
        fetchNewQuote()

        // Button listeners
        findViewById<Button>(R.id.refreshButton).setOnClickListener {



            fetchNewQuote()  // Fetch a new quote on button press
        }

        findViewById<ImageButton>(R.id.shareButton).setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "$currentQuote - $currentAuthor")
            }
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }

        findViewById<Button>(R.id.saveButton).setOnClickListener {
            // Create a Quote object and save it to favorites
            val quote = Quote(currentQuote, currentAuthor)
            SharedPreferencesHelper.saveFavoriteQuote(this, quote)

            // Log the saved quote to check
            Log.d("MainActivity", "Quote saved: $currentQuote by $currentAuthor")

            // Show a toast
            Toast.makeText(this, "Quote saved!", Toast.LENGTH_SHORT).show()

        }



    }

    private fun addQuotesManually(quotes: List<Quote>) {
        val sharedPreferences = getSharedPreferences("your_prefs_name", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Convert the quotes list to JSON and save it in SharedPreferences
        val json = Gson().toJson(quotes)
        editor.putString(sharedPreferencesKey, json)
        editor.apply()

        Log.d("MainActivity", "Manually added quotes: $json")
    }

    private fun displayRandomQuote() {
        val sharedPreferences = getSharedPreferences("your_prefs_name", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(sharedPreferencesKey, null)

        if (!json.isNullOrEmpty()) {
            // Deserialize JSON to List<Quote>
            val quotesList = Gson().fromJson(json, Array<Quote>::class.java).toList()

            // Pick a random quote if the list is not empty
            if (quotesList.isNotEmpty()) {
                val randomQuote = quotesList.random()
                val quoteText = randomQuote.quoteText ?: "No Quote Available"
                val authorText = randomQuote.author ?: "Unknown Author"

                // Update the TextViews with the selected quote
                quoteTextView.text = quoteText
                authorTextView.text = authorText

                Log.d("MainActivity", "Displayed quote: $quoteText by $authorText")
            }
        } else {
            // Default text if no quotes are found
            quoteTextView.text = "No Quote Available"
            authorTextView.text = "Unknown Author"
        }
    }

    private fun fetchNewQuote() {
        val apiService = ApiServiceBuilder.apiService.getQuote()
        apiService.enqueue(object : Callback<List<QuoteResponse>> {


            override fun onResponse(
                call: Call<List<QuoteResponse>>,
                response: Response<List<QuoteResponse>>,
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val quotes = response.body()!!
                    if (quotes.isNotEmpty()) {
                        val quote = quotes[0]  // Using the first quote in the list
                        quoteTextView.text = quote.q
                        authorTextView.text = quote.a
                    } else {
                        Toast.makeText(this@MainActivity, "No quotes available", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to load quote", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<QuoteResponse>>, t: Throwable) {
                TODO("Not yet implemented")
            }


        })

}

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }
}
