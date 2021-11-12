package com.example.simpletodo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText

class EditActivity : AppCompatActivity() {

    lateinit var etItem: EditText
    lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)


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
}