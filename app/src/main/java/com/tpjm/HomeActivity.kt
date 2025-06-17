package com.tpjm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        findViewById<Button>(R.id.btnSearch).setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        findViewById<Button>(R.id.btnTranslate).setOnClickListener {
            startActivity(Intent(this, TranslateActivity::class.java))
        }

        val btnNauka = findViewById<Button>(R.id.btnLearn)
        btnNauka.setOnClickListener {
            val intent = Intent(this, ChooseLevelActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.btnExit).setOnClickListener {
            finishAffinity()
        }
    }
}
