package com.example.simpletodo

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat

class ThemeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme)


        findViewById<Button>(R.id.btn_red).setOnClickListener {
            //change theme to red
            setTheme(R.style.Theme_SimpleToDo_Red)
            setContentView(R.layout.activity_theme)
            supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this,R.color.red_800)))

        }
        findViewById<Button>(R.id.btn_green).setOnClickListener {
            setTheme(R.style.Theme_SimpleToDo_Green)
            setContentView(R.layout.activity_theme)
            supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this,R.color.green_800)))
        }
        findViewById<Button>(R.id.btn_blue).setOnClickListener {
            setTheme(R.style.Theme_SimpleToDo_Blue)
            setContentView(R.layout.activity_theme)
            supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this,R.color.blue_700)))

        }

    }
}