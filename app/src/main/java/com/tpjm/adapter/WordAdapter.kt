package com.tpjm.adapter  // <- Lepsza organizacja kodu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tpjm.R
import com.tpjm.model.Word

class WordAdapter(
    private var words: List<Word>,
    private val onItemClick: (Word) -> Unit
) : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wordTextView: TextView = itemView.findViewById(R.id.wordTextView1)

        fun bind(word: Word) {
            wordTextView.text = word.word
            itemView.setOnClickListener { onItemClick(word) }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_word, parent, false)
        return WordViewHolder(view)
    }
    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(words[position])
    }
    override fun getItemCount(): Int = words.size
    fun updateWords(newWords: List<Word>) {
        words = newWords
        notifyDataSetChanged()
    }
}