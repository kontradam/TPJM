package com.tpjm

import android.net.Uri
import android.os.Bundle
import android.widget.Button
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
import com.tpjm.model.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LessonActivity : AppCompatActivity() {

    private lateinit var lessonHeader: TextView
    private lateinit var playerView: PlayerView
    private lateinit var btnPlayAll: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var exoPlayer: ExoPlayer

    private lateinit var wordAdapter: WordAdapter
    private var words: List<Word> = listOf()
    private var playJob: Job? = null
    private var level: String = ""
    private var lessonNumber: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)

        lessonHeader = findViewById(R.id.lessonHeader)
        playerView = findViewById(R.id.playerView)
        btnPlayAll = findViewById(R.id.btnPlayAll)
        recyclerView = findViewById(R.id.wordsRecyclerView)
        exoPlayer = ExoPlayer.Builder(this).build()
        playerView.player = exoPlayer

        level = intent.getStringExtra("LEVEL") ?: "A1"
        lessonNumber = intent.getIntExtra("LESSON", 1)

        lessonHeader.text = "Poziom $level - Lekcja $lessonNumber"

        recyclerView.layoutManager = LinearLayoutManager(this)


        val wordDao = AppDatabase.getInstance(applicationContext).wordDao()
        lifecycleScope.launch {
            words = withContext(Dispatchers.IO) {
                wordDao.getWordsForLesson(level, lessonNumber)
            }
            wordAdapter = WordAdapter(words) { word ->
                playWord(word)
            }
            recyclerView.adapter = wordAdapter
        }

        btnPlayAll.setOnClickListener {
            playAllWords()
        }
    }

    private fun playWord(word: Word) {
        val videoResId = resources.getIdentifier(word.videoPath, "raw", packageName)
        if (videoResId != 0) {
            val videoUri = Uri.parse("android.resource://$packageName/$videoResId")
            val mediaItem = MediaItem.fromUri(videoUri)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.play()
        } else {
            Toast.makeText(this, "Brak wideo dla: ${word.word}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playAllWords() {
        playJob?.cancel()
        playJob = lifecycleScope.launch {
            for (word in words) {
                playWord(word)
                /*delay(2200)
                exoPlayer.stop() */
            }
        }
    }

    override fun onStop() {
        super.onStop()
        exoPlayer.release()
        playJob?.cancel()
    }
}
