package com.tpjm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class ChooseLevelActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_level)

        fun openLessons(level: String) {
            val intent = Intent(this, ChooseLessonActivity::class.java)
            intent.putExtra("LEVEL", level)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnA1).setOnClickListener {
            openLessons("A1")
        }

        findViewById<Button>(R.id.btnA2).setOnClickListener {
            openLessons("A2")

        findViewById<Button>(R.id.btnB1).setOnClickListener {
            openLessons("B1")
        }
        findViewById<Button>(R.id.btnB2).setOnClickListener {
            openLessons("B2")
        }


    }


}
}