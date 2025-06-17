package com.tpjm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ChooseLessonActivity : AppCompatActivity() {
    private var level: String = "A1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_lesson)

        level = intent.getStringExtra("LEVEL") ?: "A1"

        findViewById<Button>(R.id.btnLesson1).setOnClickListener { openLesson(1) }
        findViewById<Button>(R.id.btnLesson2).setOnClickListener { openLesson(2) }
        findViewById<Button>(R.id.btnLesson3).setOnClickListener { openLesson(3) }
        findViewById<Button>(R.id.btnLesson4).setOnClickListener { openLesson(4) }
        findViewById<Button>(R.id.btnLesson5).setOnClickListener { openLesson(5) }
        findViewById<Button>(R.id.btnLesson6).setOnClickListener { openLesson(6) }
        findViewById<Button>(R.id.btnLesson7).setOnClickListener { openLesson(7) }
        findViewById<Button>(R.id.btnLesson8).setOnClickListener { openLesson(8) }
        findViewById<Button>(R.id.btnLesson9).setOnClickListener { openLesson(9) }
        findViewById<Button>(R.id.btnLesson10).setOnClickListener { openLesson(10) }
    }

    private fun openLesson(lessonNumber: Int) {
        val intent = Intent(this, LessonActivity::class.java)
        intent.putExtra("LEVEL", level)
        intent.putExtra("LESSON", lessonNumber)
        startActivity(intent)
    }
}