package com.tpjm

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import com.tpjm.adapter.WordAdapter
import com.tpjm.data.AppDatabase
import com.tpjm.data.WordDao
import com.tpjm.model.Word
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

class SearchActivity : AppCompatActivity() {

    private lateinit var wordAdapter: WordAdapter
    private lateinit var wordDao: WordDao
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var wordTextView: TextView
    private lateinit var playerView: PlayerView
    private lateinit var exoPlayer: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)
        wordTextView = findViewById(R.id.wordTextView1)
        playerView = findViewById(R.id.playerView)
        exoPlayer = ExoPlayer.Builder(this).build()
        playerView.player = exoPlayer

        recyclerView.layoutManager = LinearLayoutManager(this)
        wordAdapter = WordAdapter(emptyList()) { word -> playVideo(word) }
        recyclerView.adapter = wordAdapter

        val db = AppDatabase.getInstance(applicationContext)
        wordDao = db.wordDao()

        importWordsFromCSV()

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            searchWord(query)
        }
    }

    private fun importWordsFromCSV() {
        val inputStream = resources.openRawResource(R.raw.slowa)
        val reader = BufferedReader(InputStreamReader(inputStream))

        lifecycleScope.launch {
            reader.useLines { lines ->
                lines.drop(1).forEach { line ->
                    val parts = line.split(";")
                    if (parts.size >= 4) {
                        val word = parts[0].trim()
                        val videoPath = parts[1].trim()
                        val level = parts[2].trim()
                        val lesson = parts[3].trim().toIntOrNull() ?: 0
                        val wordEntity = Word(word = word, videoPath = videoPath, level = level, lesson = lesson)
                        wordDao.insert(wordEntity)
                    }
                }
            }
            loadWords()
        }
    }

    private fun loadWords() {
        lifecycleScope.launch {
            val words = wordDao.getAllWords()
            Log.d("DATABASE", "Baza danych po uruchomieniu: $words")
            runOnUiThread {
                wordAdapter.updateWords(words)
            }
        }
    }

    private fun searchWord(query: String) {
        lifecycleScope.launch {
            val normalizedQuery = "%${query.trim().lowercase()}%"
            val results = wordDao.searchWords(normalizedQuery)
            runOnUiThread {
                if (results.isNotEmpty()) {
                    val first = results.first()
                    wordTextView.text = first.word
                    playVideo(first)
                } else {
                    wordTextView.text = "Nie znaleziono"
                    Toast.makeText(this@SearchActivity, "Nie znaleziono słowa w słowniku", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun playVideo(word: Word) {
        wordTextView.text = word.word

        val videoResId = resources.getIdentifier(word.videoPath, "raw", packageName)

        Log.d("VIDEO", "Video resource ID: $videoResId dla videoPath=${word.videoPath}")

        if (videoResId != 0) {
            val videoUri = Uri.parse("android.resource://$packageName/$videoResId")
            val mediaItem = MediaItem.fromUri(videoUri)

            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.play()
        } else {
            Toast.makeText(this, "Nie znaleziono wideo!", Toast.LENGTH_SHORT).show()
            Log.e("VIDEO", "Błąd: nie znaleziono pliku ${word.videoPath} w res/raw")
        }
    }

    override fun onStop() {
        super.onStop()
        exoPlayer.release()
    }
}
