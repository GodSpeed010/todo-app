package com.example.simpletodo

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat

class EditActivity : AppCompatActivity() {

    lateinit var etItem: EditText
    lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        //set the theme
        setThemeTo(intent.getStringExtra(MainActivity().COLOR_THEME))

        etItem = findViewById(R.id.etItem)
        btnSave = findViewById(R.id.btnSave)

        supportActionBar?.title = "Edit Item"

        etItem.setText(intent.getStringExtra(MainActivity().KEY_ITEM_TEXT))

        // When the user is done editing, they click the save button
        btnSave.setOnClickListener {

            //pass the data (results of editing)
            intent.putExtra(MainActivity().KEY_ITEM_TEXT, etItem.text.toString())
            intent.putExtra(MainActivity().KEY_ITEM_POSITION, intent.extras?.getInt(MainActivity().KEY_ITEM_POSITION))

            //set the result of the intent
            setResult(RESULT_OK, intent)

            //finish the activity, close the screen and go back
            finish()
        }
    }

    fun setThemeTo(theme: String?) {
        when (theme) {
            "red" -> {
                setTheme(R.style.Theme_SimpleToDo_Red)
                supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this,R.color.red_800)))
                findViewById<Button>(R.id.btnSave).setBackgroundColor(resources.getColor(R.color.red_800))

                if (Build.VERSION.SDK_INT >= 21) {
                    window.navigationBarColor = resources.getColor(R.color.red_800)
                    window.statusBarColor = resources.getColor(R.color.red_800)
                }
            }
            "green" -> {
                setTheme(R.style.Theme_SimpleToDo_Green)
                supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this,R.color.green_800)))
                findViewById<Button>(R.id.btnSave).setBackgroundColor(resources.getColor(R.color.green_800))

                if (Build.VERSION.SDK_INT >= 21) {
                    window.navigationBarColor = resources.getColor(R.color.green_800)
                    window.statusBarColor = resources.getColor(R.color.green_800)
                }
            }
            "blue" -> {
                setTheme(R.style.Theme_SimpleToDo_Blue)
                supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this,R.color.blue_700)))
                findViewById<Button>(R.id.btnSave).setBackgroundColor(resources.getColor(R.color.blue_700))

                if (Build.VERSION.SDK_INT >= 21) {
                    window.navigationBarColor = resources.getColor(R.color.blue_700)
                    window.statusBarColor = resources.getColor(R.color.blue_700)
                }
            }
            else -> {
                setTheme(R.style.Theme_SimpleToDo)
                supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this,R.color.purple_500)))
                findViewById<Button>(R.id.btnSave).setBackgroundColor(resources.getColor(R.color.purple_500))

                if (Build.VERSION.SDK_INT >= 21) {
                    window.navigationBarColor = resources.getColor(R.color.purple_500)
                    window.statusBarColor = resources.getColor(R.color.purple_500)
                }
            }
        }

    }
}