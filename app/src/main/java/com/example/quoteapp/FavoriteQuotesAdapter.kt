package com.example.quoteapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FavoriteQuotesAdapter(private val quotes: List<Quote>) : RecyclerView.Adapter<FavoriteQuotesAdapter.QuoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_quote, parent, false)
        return QuoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val quote = quotes[position]

        // Safely handle the quote text and author, defaulting to "No Quote Available" or "Unknown Author"
        val quoteText = quote.quoteText ?: "No Quote Available"
        val authorText = quote.author ?: "Unknown Author"

        Log.d("FavoriteQuotesAdapter", "quoteText: $quoteText, authorText: $authorText")


        // Bind the data to the ViewHolder
        holder.bind(quoteText, authorText)
    }

    override fun getItemCount(): Int = quotes.size

    class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val quoteTextView: TextView = itemView.findViewById(R.id.quote)
        private val authorTextView: TextView = itemView.findViewById(R.id.auth)

        fun bind(quoteText: String, authorText: String) {
            // Safely set the quote text and author text
            quoteTextView.text = quoteText
            authorTextView.text = authorText
        }
    }
}
