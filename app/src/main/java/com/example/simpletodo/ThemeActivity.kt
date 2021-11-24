package com.example.simpletodo

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.core.content.ContextCompat

class ThemeActivity : AppCompatActivity() {

    var colorTheme: String = "default"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme)

        // String with default theme color
        // Set a default theme
        // If button is click
            // Change theme
            // Change value in string
        // When Intent is created
            // Send theme color
        // Inside new Intent
            // Have a function that is called in the beginning
            // function reads the string using getIntent()
            // sets theme for NewActivity


        findViewById<Button>(R.id.btn_red).setOnClickListener {
            //change theme to red
            setTheme(R.style.Theme_SimpleToDo_Red)
            supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this,R.color.red_800)))

            if (Build.VERSION.SDK_INT >= 21) {
                window.navigationBarColor = resources.getColor(R.color.red_800)
                window.statusBarColor = resources.getColor(R.color.red_800)
            }
            //update theme variable
            colorTheme = "red"
        }
        findViewById<Button>(R.id.btn_green).setOnClickListener {
            //change theme to green
            setTheme(R.style.Theme_SimpleToDo_Green)
            supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this,R.color.green_800)))

            if (Build.VERSION.SDK_INT >= 21) {
                window.navigationBarColor = resources.getColor(R.color.green_800)
                window.statusBarColor = resources.getColor(R.color.green_800)
            }

            colorTheme = "green"
        }
        findViewById<Button>(R.id.btn_blue).setOnClickListener {
            //change theme to blue
            setTheme(R.style.Theme_SimpleToDo_Blue)
            supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this,R.color.blue_700)))

            if (Build.VERSION.SDK_INT >= 21) {
                window.navigationBarColor = resources.getColor(R.color.blue_700)
                window.statusBarColor = resources.getColor(R.color.blue_700)
            }

            colorTheme = "blue"
        }

        findViewById<Button>(R.id.btn_default).setOnClickListener {
            //change theme to blue
            setTheme(R.style.Theme_SimpleToDo)
            supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this,R.color.purple_500)))

            if (Build.VERSION.SDK_INT >= 21) {
                window.navigationBarColor = resources.getColor(R.color.purple_500)
                window.statusBarColor = resources.getColor(R.color.purple_500)
            }

            colorTheme = "default"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu) : Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_theme, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar items
        when (item.getItemId()) {
            R.id.miSave -> {
                Log.d("my_tag", "Save button pressed")

                //send back the theme String
                intent.putExtra(MainActivity().COLOR_THEME, colorTheme)
                setResult(RESULT_OK, intent)
                finish()

                return true
            }
            else ->
                return super.onOptionsItemSelected(item)
        }
    }

}