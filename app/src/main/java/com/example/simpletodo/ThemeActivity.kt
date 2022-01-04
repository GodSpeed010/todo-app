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
import com.example.simpletodo.databinding.ActivityThemeBinding

class ThemeActivity : AppCompatActivity() {

    private val TAG = "ThemeActivity"
    lateinit var binding: ActivityThemeBinding

    lateinit var colorTheme: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThemeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        colorTheme = intent.getStringExtra(MainActivity().COLOR_THEME).toString()
        setThemeTo(colorTheme)
        Log.d(TAG, "colorTheme is $colorTheme")

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


        binding.btnRed.setOnClickListener {
            //change theme to red
            setThemeTo("red")
        }
        binding.btnGreen.setOnClickListener {
            //change theme to green
            setThemeTo("green")
        }
        binding.btnBlue.setOnClickListener {
            //change theme to blue
            setThemeTo("blue")
        }

        binding.btnDefault.setOnClickListener {
            //change theme to blue
            setThemeTo("default")
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
                Log.d(TAG, "Save button pressed")

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

    fun setThemeTo(theme: String?) {
        when (theme) {
            "red" -> {
                setTheme(R.style.Theme_SimpleToDo_Red)
                supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this,R.color.red_800)))
                if (Build.VERSION.SDK_INT >= 21) {
                    window.navigationBarColor = resources.getColor(R.color.red_800)
                    window.statusBarColor = resources.getColor(R.color.red_800)
                }
                colorTheme = "red"
            }
            "green" -> {
                setTheme(R.style.Theme_SimpleToDo_Green)
                supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this,R.color.green_800)))
                if (Build.VERSION.SDK_INT >= 21) {
                    window.navigationBarColor = resources.getColor(R.color.green_800)
                    window.statusBarColor = resources.getColor(R.color.green_800)
                }
                colorTheme = "green"
            }
            "blue" -> {
                setTheme(R.style.Theme_SimpleToDo_Blue)
                supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this,R.color.blue_700)))
                if (Build.VERSION.SDK_INT >= 21) {
                    window.navigationBarColor = resources.getColor(R.color.blue_700)
                    window.statusBarColor = resources.getColor(R.color.blue_700)
                }
                colorTheme = "blue"
            }
            else -> {
                setTheme(R.style.Theme_SimpleToDo)
                supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this,R.color.purple_500)))
                if (Build.VERSION.SDK_INT >= 21) {
                    window.navigationBarColor = resources.getColor(R.color.purple_500)
                    window.statusBarColor = resources.getColor(R.color.purple_500)
                }
                colorTheme = "default"
            }
        }

    }

}