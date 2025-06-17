package com.tpjm

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import com.tpjm.data.AppDatabase
import com.tpjm.data.WordDao
import kotlinx.coroutines.*

class TranslateActivity : AppCompatActivity() {

    private lateinit var inputSentence: EditText
    private lateinit var btnPlayTranslation: Button
    private lateinit var playerView: PlayerView
    private lateinit var currentWord: TextView
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var wordDao: WordDao

    private var playJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translate)

        inputSentence = findViewById(R.id.inputSentence)
        btnPlayTranslation = findViewById(R.id.btnPlayTranslation)
        playerView = findViewById(R.id.playerView)
        currentWord = findViewById(R.id.currentWord)
        exoPlayer = ExoPlayer.Builder(this).build()
        playerView.player = exoPlayer

        wordDao = AppDatabase.getInstance(applicationContext).wordDao()

        btnPlayTranslation.setOnClickListener {
            val sentence = inputSentence.text.toString()
            playTranslation(sentence)
        }
    }

    private fun playTranslation(sentence: String) {
        playJob?.cancel()
        val words = sentence.trim().split("\\s+".toRegex())
        if (words.isEmpty()) return

        playJob = lifecycleScope.launch {
            for (word in words) {
                val wordEntity = withContext(Dispatchers.IO) {
                    wordDao.searchWords("%${word.lowercase()}%").firstOrNull()
                }
                if (wordEntity != null) {
                    currentWord.text = wordEntity.word
                    val videoResId = resources.getIdentifier(wordEntity.videoPath, "raw", packageName)
                    if (videoResId != 0) {
                        val videoUri = Uri.parse("android.resource://$packageName/$videoResId")
                        val mediaItem = MediaItem.fromUri(videoUri)
                        exoPlayer.setMediaItem(mediaItem)
                        exoPlayer.prepare()
                        exoPlayer.play()
                        delay(5000)
                    } else {
                        currentWord.text = "Brak filmu dla: ${wordEntity.word}"
                        Toast.makeText(this@TranslateActivity, "Brak wideo dla: ${wordEntity.word}", Toast.LENGTH_SHORT).show()
                        delay(1000)
                    }
                } else {
                    currentWord.text = "Brak słowa: $word"
                    Toast.makeText(this@TranslateActivity, "Brak słowa: $word", Toast.LENGTH_SHORT).show()
                    delay(1000)
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        exoPlayer.release()
        playJob?.cancel()
    }
}
